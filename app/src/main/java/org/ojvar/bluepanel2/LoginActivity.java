package org.ojvar.bluepanel2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.ProgressHelper;
import org.ojvar.bluepanel2.Helpers.SettingHelper;
import org.ojvar.bluepanel2.Helpers.ToastHelper;
import org.ojvar.bluepanel2.Helpers.SecurityHelper;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText passwordEditText;

    /**
     * OnCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    /**
     * On Resume - Reload settings
     */
    @Override
    protected void onResume() {
        super.onResume();

        BluetoothHelper.disconnect();
        SettingHelper.loadSetting();
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
        loginButton = findViewById(R.id.loginButton);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(checkLogin);
        findViewById(R.id.settingButton).setOnClickListener(showSettingActivity);
    }

    /**
     * Show Setting activity
     */
    private View.OnClickListener showSettingActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, SettingActivity.class);

            startActivity(intent);
        }
    };

    /**
     * Check Login
     */
    private final View.OnClickListener checkLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pwd = passwordEditText.getText()
                    .toString();

            /* Disable UI */
            disableUI();

            boolean attemptResult = SecurityHelper.attempt(pwd);

            if (!attemptResult) {
                ToastHelper.showNotify(getString(R.string.invalid_password));

                enableUI();
            } else {
                String deviceId = GlobalData.settings.getDeviceId() + "";

                if (deviceId.length() == 0) {
                    ToastHelper.showNotify(getString(R.string.device_not_found));

                    enableUI();
                } else {
                    final Handler handler = new Handler();

                    BluetoothHelper.connect(deviceId, new BluetoothHelper.BluetoothEvents() {
                        @Override
                        public void OnConnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastHelper.showNotify(getString(R.string.connection_successfully));

                                    showMainActivity();
                                }
                            });
                        }

                        @Override
                        public void OnDisconnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    enableUI();

                                    ToastHelper.showNotify(getString(R.string.connection_failed));
                                }
                            });
                        }

                        @Override
                        public void OnCommand(String data) {
                        }
                    });
                }
            }
        }
    };

    /**
     * Set UI State
     *
     * @param state
     */
    private void setUIState(boolean state) {
        loginButton.setEnabled(state);
        passwordEditText.setEnabled(state);
    }

    /**
     * Enable UI
     */
    private void enableUI() {
        setUIState(true);
        ProgressHelper.hideProgress();
    }

    /**
     * Disable UI
     */
    private void disableUI() {
        setUIState(false);
        ProgressHelper.showProgress(LoginActivity.this,
                "", getString(R.string.conneting_to_device));
    }

    /**
     * Show Main activity
     */
    private void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
