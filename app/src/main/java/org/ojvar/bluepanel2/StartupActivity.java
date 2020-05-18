package org.ojvar.bluepanel2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.SettingHelper;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boot();
    }

    /**
     * Boot
     */
    public void boot() {
        /* Prepare */
        this.prepare();

        /* Run login activity */
        Intent intent = new Intent(StartupActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     * Prepare
     */
    private void prepare() {
        /* Setup application context */
        GlobalData.applicationContext = getApplicationContext();

        /* Load settings */
        SettingHelper.loadSetting();

        /* Prepare bluetooth adapter */
        if (!BluetoothHelper.init()) {
            finish();
        }
    }
}
