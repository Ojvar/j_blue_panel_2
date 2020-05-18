package org.ojvar.bluepanel2.App;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import org.ojvar.bluepanel2.Models.SettingModel;

public class GlobalData {
    public static Context applicationContext;
    public static BluetoothAdapter btAdapter;

    public static SettingModel settings = new SettingModel();
}
