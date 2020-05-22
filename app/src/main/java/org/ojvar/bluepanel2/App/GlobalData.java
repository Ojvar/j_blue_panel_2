package org.ojvar.bluepanel2.App;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.ToastHelper;
import org.ojvar.bluepanel2.Models.SettingModel;

public class GlobalData {
    public static Context applicationContext;
    public static BluetoothAdapter btAdapter;
    public static BluetoothDevice btDevice;
    public static BluetoothSocket btSocket;

    public static SettingModel settings = new SettingModel();


    /**
     * Setup bluetooth event handler
     */
    public static void clearBTEventHandler() {
        BluetoothHelper.events = null;
    }

    /**
     * Setup bluetooth event handler
     */
    public static void setupBTEventHandler(BluetoothHelper.BluetoothEvents events) {
        BluetoothHelper.events = events;
    }

}
