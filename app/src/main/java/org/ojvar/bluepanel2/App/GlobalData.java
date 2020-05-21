package org.ojvar.bluepanel2.App;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import org.ojvar.bluepanel2.Models.SettingModel;

import java.net.Socket;

public class GlobalData {
    public static Context applicationContext;
    public static BluetoothAdapter btAdapter;
    public static BluetoothDevice btDevice;
    public static BluetoothSocket btSocket;

    public static SettingModel settings = new SettingModel();
}
