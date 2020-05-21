package org.ojvar.bluepanel2.Helpers;

import android.bluetooth.BluetoothAdapter;

import org.ojvar.bluepanel2.App.GlobalData;

import java.io.IOException;
import java.util.UUID;

public class BluetoothHelper {
    private static UUID MY_UUID = UUID.fromString(
            "00001101-0000-1000-8000-00805F9B34FB"
    );

    /**
     * Bluetooth events
     */
    public interface BluetoothEvents {
        void OnConnect();

        void OnDisconnect();
    }


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

    /**
     * Connect to device
     *
     * @param deviceId
     * @return
     */
    public static boolean connect(String deviceId, final BluetoothEvents events) {
        if (null == GlobalData.btAdapter) {
            return false;
        }

        GlobalData.btDevice = GlobalData.btAdapter.getRemoteDevice(deviceId);
        if (GlobalData.btDevice == null) {
            ToastHelper.showNotify("No device found");

            return false;
        }

        /**
         * Try to connect
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Try to connect
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

                // Cancel Discovery
                GlobalData.btAdapter.cancelDiscovery();

                // Get bluetooth device
                GlobalData.btDevice = GlobalData.btAdapter.getRemoteDevice(GlobalData.settings.getDeviceId());

                try {
                    GlobalData.btSocket = GlobalData.btDevice.createInsecureRfcommSocketToServiceRecord(uuid);

                    GlobalData.btSocket.connect();

                    if (!GlobalData.btSocket.isConnected()) {
                        throw new Exception("Connection failed");
                    }

                    GlobalData.btSocket.getOutputStream().write("Hello to you".getBytes());

                    // Listen to socket
//                    startListener(socket);

                    if (null != events) {
                        events.OnConnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    if (null != events) {
                        events.OnDisconnect();
                    }
                }
            }
        }).start();

        return true;
    }
}
