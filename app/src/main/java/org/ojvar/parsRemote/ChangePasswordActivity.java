package org.ojvar.parsRemote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import org.ojvar.parsRemote.App.GlobalData;
import org.ojvar.parsRemote.Helpers.ToastHelper;
import org.ojvar.parsRemote.Helpers.VibrationHelper;

public class ChangePasswordActivity extends AppCompatActivity {
    /**
     * On Create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setup();
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
        findViewById(R.id.resetButton).setOnClickListener(backButtonClick);
        findViewById(R.id.saveButton).setOnClickListener(saveButtonClick);
    }

    /**
     * Change Password
     */
    private boolean changePassword() {
        Context context = this;

        String storedOldPassword = GlobalData.settings.getPassword();
        String oldPassword = ((EditText) findViewById(R.id.oldPasswordEditText))
                .getText()
                .toString() + "";
        String newPassword = ((EditText) findViewById(R.id.newPasswordEditText))
                .getText()
                .toString() + "";
        String confirmNewPassword = ((EditText) findViewById(R.id.confirmNewPasswordEditText))
                .getText()
                .toString() + "";

        int passMinLen = getResources().getInteger(R.integer.password_min_len);
        int passMaxLen = getResources().getInteger(R.integer.password_max_len);

        if (oldPassword.equals(storedOldPassword)) {
            if (newPassword.equals(confirmNewPassword)) {
                if (newPassword.length() < passMinLen) {
                    ToastHelper.showNotify(String.format(getString(R.string.min_pass_len),
                            String.valueOf(passMinLen)), context);
                } else if (newPassword.length() > passMaxLen) {
                    ToastHelper.showNotify(String.format(getString(R.string.max_pass_len),
                            String.valueOf(passMaxLen)), context);
                } else {
                    GlobalData.settings.setPassword(newPassword);

                    return true;
                }
            } else {
                ToastHelper.showNotify(getString(R.string.check_new_password), context);
            }
        } else {
            ToastHelper.showNotify(getString(R.string.invalid_old_pass), context);
        }

        return false;
    }

    /**
     * Save button click
     */
    private final OnClickListener saveButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            if (changePassword()) {
                finish();
            }
        }
    };

    /**
     * Back button click
     */
    private final OnClickListener backButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            finish();
        }
    };
}
