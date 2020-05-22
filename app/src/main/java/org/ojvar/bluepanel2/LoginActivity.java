package org.ojvar.bluepanel2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.SettingHelper;
import org.ojvar.bluepanel2.Helpers.ToastHelper;
import org.ojvar.bluepanel2.Helpers.SecurityHelper;

public class LoginActivity extends AppCompatActivity {

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
        findViewById(R.id.loginButton).setOnClickListener(checkLogin);
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
            EditText passwordEditText = findViewById(R.id.passwordEditText);
            String pwd = passwordEditText.getText()
                    .toString();

            boolean attemptResult = SecurityHelper.attempt(pwd);

            if (!attemptResult) {
                ToastHelper.showNotify("Invalid Password");
            } else {
                String deviceId = GlobalData.settings.getDeviceId() + "";

                if (deviceId.length() == 0) {
                    ToastHelper.showNotify("No bluetooth device selected");
                } else {
                    final Handler handler = new Handler();

                    BluetoothHelper.connect(deviceId, new BluetoothHelper.BluetoothEvents() {
                        @Override
                        public void OnConnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastHelper.showNotify("Connection successfully");

                                    showMainActivity();
                                }
                            });
                        }

                        @Override
                        public void OnDisconnect() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastHelper.showNotify("Connection failed, disconnected");
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
     * Show Main activity
     */
    private void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
