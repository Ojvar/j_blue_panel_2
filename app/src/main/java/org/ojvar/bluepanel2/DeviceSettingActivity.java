package org.ojvar.bluepanel2;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.BaseActivity;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.MinMaxFilter;
import org.ojvar.bluepanel2.Helpers.SettingHelper;

import static org.ojvar.bluepanel2.App.GlobalData.setupBTEventHandler;

public class DeviceSettingActivity extends BaseActivity {
    private Handler handler = new Handler();

    /**
     * On Create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);

        setup();
    }

    /**
     * On Resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        setupBTEventHandler(btEvents);
    }

    /**
     * Setup
     */
    private void setup() {
        prepareEdits();
        bindEvents();
        updateUI();
    }

    /**
     * Prepare editViews
     */
    private void prepareEdits() {
        for (int i = 1; i < 21; i++) {
            String resName = "param" + i + "EditText";

            EditText view = (EditText) findViewByName(resName);
            if (null != view) {
                view.setFilters(new InputFilter[]{
                        new MinMaxFilter(getString(R.string.setting_min_value),
                                getString(R.string.setting_max_value))
                });
            }
        }
    }

    /**
     * Bind events
     */
    private void bindEvents() {
        findViewById(R.id.resetButton).setOnClickListener(resetDataClick);
        findViewById(R.id.backButton).setOnClickListener(backButtonClick);
        findViewById(R.id.loadButton).setOnClickListener(loadButtonClick);
        findViewById(R.id.saveButton).setOnClickListener(saveButtonClick);
    }

    /**
     * Load button click
     */
    private View.OnClickListener loadButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BluetoothHelper.send(getString(R.string.cmd_status));
        }
    };

    /**
     * Save button click
     */
    private View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            syncData();

            finish();
        }
    };

    /**
     * Save button click
     */
    private View.OnClickListener resetDataClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 1; i < 10; ++i) {
                String resName = "param" + i + "EditText";
                String value = getStringResourceByName(resName);

                GlobalData.dataModel.setValue(resName, value);
            }

            /* Update UI */
            updateUI();

            syncData();
        }
    };

    /**
     * Sync data
     */
    private void syncData() {
        /* Send to device */
        String cmd = collectData();
        BluetoothHelper.send(cmd);

        /* Update data mode l*/
        updateDataModel();
    }

    /**
     * Get resource by name
     *
     * @param aString
     * @return
     */
    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();

        int resId = getResources()
                .getIdentifier(aString, "string", packageName);

        return getString(resId);
    }

    /**
     * Collect data
     *
     * @return
     */
    private String collectData() {
        /* Generate command string */
        String cmd = "";

        String[] params = new String[20];
        for (int i = 1; i < 21; i++) {
            String resName = "param" + i + "EditText";

            EditText view = (EditText) findViewByName(resName);
            if (null != view) {
                String value = view.getText()
                        .toString();

                /* Add to array */
                params[i - 1] = value;
            }
        }

        for (int i = 0; i < 19; ++i) {
            cmd += params[i] + ";";
        }
        cmd += params[19];

        cmd = getString(R.string.cmd_params, cmd);

        return cmd;
    }

    /**
     * Back button click
     */
    private View.OnClickListener backButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * BTEvents
     */
    private BluetoothHelper.BluetoothEvents btEvents = new BluetoothHelper.BluetoothEvents() {
        @Override
        public void OnConnect() {

        }

        @Override
        public void OnDisconnect() {

        }

        @Override
        public void OnCommand(final String data) {
            final String[] params = GlobalData.updateDataModel(data);

            if (params.length < 21) {
                for (int i = 0; i < params.length; ++i) {
                    final int j = i;

                    /* Update UI */
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateUI(params[j]);
                        }
                    });
                }
            }
        }
    };

    /**
     * Update DataModel
     */
    private void updateDataModel() {
        for (int i = 1; i < 21; i++) {
            String resName = "param" + i + "EditText";

            EditText view = (EditText) findViewByName(resName);
            if (null != view) {
                String value = view.getText()
                        .toString();

                /* Send to dataModel */
                GlobalData.dataModel.setValue("param" + i + "EditText", value);
            }
        }

        /* Save settings to shared-preferences */
        SettingHelper.saveSetting();
    }

    /**
     * Update UI
     *
     * @param resName
     */
    private void updateUI(final String resName) {
        View view = findViewByName(resName);

        if (null != view) {
            String value = GlobalData.dataModel.getValue(resName, "");

            ((EditText) view).setText(value);
        }
    }

    /**
     * Update UI - All
     */
    private void updateUI() {
        for (int i = 1; i < 21; ++i) {
            final String resName = "param" + i + "EditText";

            updateUI(resName);
        }
    }
}

