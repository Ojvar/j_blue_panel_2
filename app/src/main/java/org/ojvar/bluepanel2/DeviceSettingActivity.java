package org.ojvar.bluepanel2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.ojvar.bluepanel2.Helpers.BaseActivity;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.ToastHelper;

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
        bindEvents();
    }

    /**
     * Bind events
     */
    private void bindEvents() {
        findViewById(R.id.backButton).setOnClickListener(backButtonClick);
        findViewById(R.id.saveButton).setOnClickListener(saveButtonClick);

        for (int i = 10; i <= 20; i++) {
            String resName = "param" + i + "EditText";

            View view = findViewByName(resName);
            if (null != view) {
                view.setOnFocusChangeListener(onFocusChangeEvent);
            }
        }
    }

    /**
     * On focus changed
     */
    private View.OnFocusChangeListener onFocusChangeEvent = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                ToastHelper.showNotify(((EditText) v).getText().toString());
            }
        }
    };

    /**
     * Save button click
     */
    private View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            finish();
        }
    };

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
            final String[] params = data.replace("[", "")
                    .replace("]", "")
                    .split(";");

            if (params.length < 20) {
                for (int i = 0; i < params.length; ++i) {
                    final int j = i + 1;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String resName = "param" + j + "EditText";

                            final View view = findViewByName(resName);

                            if (null != view) {
                                Log.i("SETTINGS", params[j-1]);
                                ((EditText) view).setText(params[j-1]);
                            }
                        }
                    });
                }
            }
        }
    };
}

