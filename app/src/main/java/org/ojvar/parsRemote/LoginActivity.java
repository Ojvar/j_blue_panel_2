package org.ojvar.parsRemote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ojvar.parsRemote.App.GlobalData;
import org.ojvar.parsRemote.Helpers.BluetoothHelper;
import org.ojvar.parsRemote.Helpers.ProgressHelper;
import org.ojvar.parsRemote.Helpers.SettingHelper;
import org.ojvar.parsRemote.Helpers.ToastHelper;
import org.ojvar.parsRemote.Helpers.SecurityHelper;
import org.ojvar.parsRemote.Helpers.VibrationHelper;

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

        GlobalData.applicationContext = this;
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
        final Context context = LoginActivity.this;

        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            String pwd = passwordEditText.getText()
                    .toString();

            /* Disable UI */
            disableUI();

            boolean attemptResult = SecurityHelper.attempt(pwd);

            if (!attemptResult) {
                ToastHelper.showNotify(getString(R.string.invalid_password), LoginActivity.this);

                enableUI();
            } else {
                String deviceId = GlobalData.settings.getDeviceId() + "";

                if (deviceId.length() == 0) {
                    ToastHelper.showNotify(getString(R.string.device_not_found), LoginActivity.this);

                    enableUI();
                } else {
                    final Handler handler = new Handler();

                    BluetoothHelper.connect(deviceId, new BluetoothHelper.BluetoothEvents() {
                        @Override
                        public void OnConnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastHelper.showNotify(getString(R.string.connection_successfully), context);

                                    LoginActivity.this.sendValidationCommand();
                                }
                            });
                        }

                        @Override
                        public void OnDisconnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    enableUI();

                                    ToastHelper.showNotify(getString(R.string.connection_failed), LoginActivity.this);
                                }
                            });
                        }

                        @Override
                        public void OnCommand(final String data) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                enableUI();
                                LoginActivity.this.checkBluetoothResponse(data);
                            }
                            });
                        }
                    }, context);
                }
            }
        }
    };

    /**
     * Checkblue tooth response
     *
     * @param data
     */
    private void checkBluetoothResponse(String data) {
        if (data.equals(getString(R.string.responseString))) {
            showMainActivity();
        } else {
//            sendValidationCommand();
            ToastHelper.showNotify(getString(R.string.connection_denied), LoginActivity.this);
        }
    }

    /**
     * Send Validation Command
     */
    private void sendValidationCommand() {
        BluetoothHelper.send(getString(R.string.tokenString));
    }

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
