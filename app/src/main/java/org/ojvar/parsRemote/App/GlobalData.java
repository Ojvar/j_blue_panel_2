package org.ojvar.parsRemote.App;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import org.ojvar.parsRemote.Helpers.BluetoothHelper;
import org.ojvar.parsRemote.Models.DataModel;
import org.ojvar.parsRemote.Models.SettingModel;
import org.ojvar.parsRemote.R;

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
        /* TODO:// OJVAR  DISALBE */
        if (true) return;

        BluetoothHelper.events = events;
    }

    /**
     * Update data model
     *
     * @param data
     */
    public static String[] updateDataModel(String data) {
        List<String> result = null;
        String[] buckets = data.replace("[", "")
                .replace("]", "")
                .replace("\n", "")
                .split("\r");

        for (String bData : buckets) {
            result = new ArrayList<>();
            final String[] params = bData.split(";");

            int paramsLen = applicationContext.getResources().getInteger(R.integer.params_len);
            if (params.length == paramsLen) {
                for (int i = 0; i < params.length; ++i) {
                    String value = params[i];

                    int nameIndex = value.indexOf(":");
                    if (-1 < nameIndex) {
                        value = value.substring(nameIndex + 1);
                    }

                    final String resName =
                            String.format(applicationContext.getString(R.string.param_edit_text_x),
                                    String.valueOf(i + 1));

                    /* Add to output list */
                    result.add(resName);

                    /* Update data model */
                    GlobalData.dataModel.setValue(resName, value);
                }
            }
        }

        String[] itemsArray = new String[0];
        if (null != result) {
            itemsArray = new String[result.size()];
            itemsArray = result.toArray(itemsArray);
        }

        return itemsArray;
    }
}
