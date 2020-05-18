package org.ojvar.bluepanel2.Helpers;

import android.bluetooth.BluetoothAdapter;

import org.ojvar.bluepanel2.App.GlobalData;

public class BluetoothHelper {
    /**
     * Initialize bluetooth adapter
     */
    public static boolean init() {
        GlobalData.btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (GlobalData.btAdapter == null) {
            ToastHelper.showNotify("Bluetooth device not found!");

            return false;
        }

        return true;
    }
}
