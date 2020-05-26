package org.ojvar.bluepanel2.App;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Models.DataModel;
import org.ojvar.bluepanel2.Models.SettingModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static Context applicationContext;
    public static BluetoothAdapter btAdapter;
    public static BluetoothDevice btDevice;
    public static BluetoothSocket btSocket;

    public static DataModel dataModel = new DataModel();
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

    /**
     * Update data model
     *
     * @param data
     */
    public static String[] updateDataModel(String data) {
        List<String> result = new ArrayList<>();
        final String[] params = data.replace("[", "")
                .replace("]", "")
                .replace("\n", "")
                .replace("\r", "")
                .split(";");

        if (params.length < 21) {
            for (int i = 0; i < params.length; ++i) {
                String value = params[i];
                final String resName = "param" + (i + 1) + "EditText";

                /* Add to output list */
                result.add(resName);

                /* Update data model */
                GlobalData.dataModel.setValue(resName, value);
            }
        }


        String[] itemsArray = new String[result.size()];
        itemsArray = result.toArray(itemsArray);

        return itemsArray;
    }
}
