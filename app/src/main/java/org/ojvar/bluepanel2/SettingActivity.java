package org.ojvar.bluepanel2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.ojvar.bluepanel2.Adapters.DevicesAdapter;
import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.SettingHelper;
import org.ojvar.bluepanel2.Helpers.ToastHelper;
import org.ojvar.bluepanel2.Models.DeviceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    private RecyclerView devicesRecycleView;
    private List<DeviceModel> devicesList = new ArrayList<>();

    /**
     * Create
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
            DeviceModel model = devicesList.get(position);

            GlobalData.settings.setDeviceId(model.getId());

            ToastHelper.showNotify("[" + model.getId() + " / " + model.getName() + "] Selected ");
        }
    };

    /**
     * Bind events
     */
    private void bindEvents() {
        devicesRecycleView = findViewById(R.id.devicesRecycleView);
        findViewById(R.id.resetButton).setOnClickListener(goBack);
        findViewById(R.id.saveButton).setOnClickListener(saveSettings);
        findViewById(R.id.chnagePasswordButton).setOnClickListener(chnagePassword);
    }
    /**
     * Change password button
     */
    private View.OnClickListener chnagePassword = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
            finish();
        }
    };

}
