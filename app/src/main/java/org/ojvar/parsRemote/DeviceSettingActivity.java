package org.ojvar.parsRemote;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.ojvar.parsRemote.Adapters.DeviceSettingsAdapter;
import org.ojvar.parsRemote.App.GlobalData;
import org.ojvar.parsRemote.Helpers.BaseActivity;
import org.ojvar.parsRemote.Helpers.BluetoothHelper;
import org.ojvar.parsRemote.Helpers.SettingHelper;
import org.ojvar.parsRemote.Helpers.VibrationHelper;
import org.ojvar.parsRemote.Models.DeviceSettingModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.ojvar.parsRemote.App.GlobalData.setupBTEventHandler;

public class DeviceSettingActivity extends BaseActivity {
    private DeviceSettingModel[] items = null;
    private RecyclerView devicesListRecyclerView = null;
    private Toast previousToast = null;

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
    }

    /**
     * Prepare editViews
     */
    private void prepareEdits() {
        loadItemsData();
        devicesListRecyclerView = findViewById(R.id.devicesListRecyclerView);

        devicesListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        devicesListRecyclerView.setLayoutManager(layoutManager);

        DeviceSettingsAdapter adapter = new DeviceSettingsAdapter(getApplicationContext(), items);
        devicesListRecyclerView.setAdapter(adapter);

        adapter.setOnItemChangeEvents(new DeviceSettingsAdapter.IOnItemChange() {
            @Override
            public void onItemValueChanged(DeviceSettingModel model, int newValue) {
                DeviceSettingModel.ValueRange range = model.getRange();

                if ((newValue < range.min) || (newValue > range.max)) {
                    newValue = range.min;
                }

                if (model.getValue() != newValue) {
                    model.setValue(newValue);

                    for (int pos = 0; pos < items.length; pos++) {
                        if (items[pos].getId() == model.getId()) {
                            updateUIItem(pos);
                            break;
                        }
                    }
                }
            }

            /**
             * onHelpeClicked
             * @param model
             * @param number
             */
            @Override
            public void onHelpClicked(DeviceSettingModel model, int number) {
                if (null != previousToast) {
                    previousToast.cancel();
                }

                previousToast = Toast.makeText(DeviceSettingActivity.this, model.getDescription(), Toast.LENGTH_LONG);
                previousToast.show();
            }
        });
    }

    /**
     * Load items data
     */
    private void loadItemsData() {
        String jsonData = getJsonData();
        items = getDevicesItems(jsonData);

        /* Update data model */
        for (DeviceSettingModel model : items) {
            int id = model.getId();
            String resName = String.format(getString(R.string.param_edit_text_x), String.valueOf(id));
            String defValue = String.valueOf(model.getRange().min);

            int value = Integer.valueOf(GlobalData.dataModel.getValue(resName, defValue));
            items[id - 1].setValue(value);
        }
    }

    /**
     * Get device items
     *
     * @param jsonData
     * @return
     */
    private DeviceSettingModel[] getDevicesItems(String jsonData) {
        Gson gson = new Gson();

        return gson.fromJson(jsonData, DeviceSettingModel[].class);
    }

    /**
     * Get Json data
     *
     * @return
     */
    private String getJsonData() {
        String jsonData = "";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(getString(R.string.fieldsJson))));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                jsonData += mLine + "\n";
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return jsonData;
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
            VibrationHelper.vibrate(getApplicationContext());

            BluetoothHelper.send(getString(R.string.cmd_status));
        }
    };

    /**
     * Save button click
     */
    private View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

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
            VibrationHelper.vibrate(getApplicationContext());

            loadItemsData();
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

        /* Update data model */
        updateDataModel(true);
    }

    /**
     * Collect data
     *
     * @return
     */
    private String collectData() {
        String cmd = "";

        for (DeviceSettingModel model : items) {
            cmd += String.valueOf(model.getValue()) + ";";
        }
        cmd = getString(R.string.cmd_params, cmd);

        return cmd;
    }

    /**
     * Back button click
     */
    private View.OnClickListener backButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            finish();
        }
    };

    /**
     * BTEvents
     */
    private BluetoothHelper.BluetoothEvents btEvents = new BluetoothHelper.BluetoothEvents() {
        public String buffer = "";

        @Override
        public void OnConnect() {
        }

        @Override
        public void OnDisconnect() {
        }

        @Override
        public synchronized void OnCommand(final String data) {
            int paramsLen = items.length;
            buffer += data;

            if (!buffer.contains("]")) {
                return;
            }

            String[] buckets = buffer.split("\n");
            buffer = "";

            for (String bucket : buckets) {
                String[] tmpParams = bucket.split(";");

                if (tmpParams.length != paramsLen) {
                    buffer += bucket + "\r\n";
                } else {
                    final String[] params = GlobalData.updateDataModel(bucket);
                    for (String param : params) {
                        int index = Integer.valueOf(param.replace("paramEditText", ""));

                        items[index].setValue(Integer.valueOf(GlobalData.dataModel.getValue(param, "0")));
                    }

                    updateUI();
                }
            }

        }
    };

    /**
     * Update DataModel
     */
    private void updateDataModel(boolean saveSetting) {
        for (int i = 0; i < items.length; i++) {
            String resName =
                    String.format(getString(R.string.param_edit_text_x), String.valueOf(i + 1));

            String value = String.valueOf(items[i].getValue());
            GlobalData.dataModel.setValue(resName, value);
        }

        /* Save settings to shared-preferences */
        if (saveSetting) {
            SettingHelper.saveSetting();
        }
    }

    /**
     * Update UI
     */
    private void updateUI() {
        if (null != devicesListRecyclerView) {
            DeviceSettingsAdapter adapter = (DeviceSettingsAdapter) devicesListRecyclerView.getAdapter();

            try {
                adapter.notifyDataSetChanged();
            } catch
            (Exception e) {
            }
        }
    }

    /**
     * Update UI
     */
    private void updateUIItem(int position) {
        if (null != devicesListRecyclerView) {
            DeviceSettingsAdapter adapter = (DeviceSettingsAdapter) devicesListRecyclerView.getAdapter();

            try {
                adapter.notifyItemChanged(position);
            } catch
            (Exception e) {
            }
        }
    }
}

