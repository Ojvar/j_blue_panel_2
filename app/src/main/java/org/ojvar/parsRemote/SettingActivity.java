package org.ojvar.parsRemote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.ojvar.parsRemote.Adapters.DevicesAdapter;
import org.ojvar.parsRemote.App.GlobalData;
import org.ojvar.parsRemote.Helpers.SettingHelper;
import org.ojvar.parsRemote.Helpers.ToastHelper;
import org.ojvar.parsRemote.Helpers.VibrationHelper;
import org.ojvar.parsRemote.Models.DeviceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    private RecyclerView devicesRecycleView;
    private List<DeviceModel> devicesList = new ArrayList<>();

    /**
     * Create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setup();
    }

    /**
     * On Resume
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Setup
     */
    private void setup() {
        bindEvents();
        fillDevicesList();
    }

    /**
     * Fill bluetooth devices list
     */
    private void fillDevicesList() {
        if (null != GlobalData.btAdapter) {
            Set<BluetoothDevice> pairedDevices = GlobalData.btAdapter.getBondedDevices();

            for (BluetoothDevice bt : pairedDevices) {
                DeviceModel model = new DeviceModel(bt.getName(), bt.getAddress());

                devicesList.add(model);
            }
        }

        devicesRecycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        devicesRecycleView.setLayoutManager(layoutManager);

        DevicesAdapter adapter = new DevicesAdapter(GlobalData.applicationContext, devicesList);
        devicesRecycleView.setAdapter(adapter);

        adapter.setClickListener(deviceItemClick);
    }

    /**
     * Device Item click
     */
    private DevicesAdapter.ItemClickListener deviceItemClick = new DevicesAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            VibrationHelper.vibrate(getApplicationContext());

            DeviceModel model = devicesList.get(position);
            GlobalData.settings.setDeviceId(model.getId());

            ToastHelper.showNotify("[" + model.getId() + " / " + model.getName() + "] Selected ", SettingActivity.this);
        }
    };

    /**
     * Bind events
     */
    private void bindEvents() {
        devicesRecycleView = findViewById(R.id.devicesRecycleView);
        findViewById(R.id.resetButton).setOnClickListener(goBack);
        findViewById(R.id.saveButton).setOnClickListener(saveSettings);
        findViewById(R.id.changePasswordButton).setOnClickListener(chnagePassword);
    }

    /**
     * Change password button
     */
    private View.OnClickListener chnagePassword = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            Intent intent = new Intent(SettingActivity.this,
                    ChangePasswordActivity.class);

            startActivity(intent);
        }
    };

    /**
     * Save button pressed
     */
    private View.OnClickListener saveSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            SettingHelper.saveSetting();
            finish();
        }
    };

    /**
     * Backbutton pressed
     */
    private View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            finish();
        }
    };

}
