package org.ojvar.bluepanel2.Helpers;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

public class BluetoothHelper {
    public static BluetoothEvents events;

    /**
     * Bluetooth events
     */
    public interface BluetoothEvents {
        void OnConnect();

        void OnDisconnect();

        void OnCommand(String data);
    }

    /**
     * Disconnect
     */
    public static void disconnect() {
        if (null != GlobalData.btSocket) {
            try {
                GlobalData.btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != GlobalData.btAdapter) {
            GlobalData.btAdapter.cancelDiscovery();
        }
    }

    /**
     * Initialize bluetooth adapter
     */
    public static boolean init() {
        GlobalData.btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (GlobalData.btAdapter == null) {
            String msg = GlobalData.applicationContext.getString(R.string.bluetooth_not_found);
            ToastHelper.showNotify(msg);

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
            String msg = GlobalData.applicationContext.getString(R.string.device_not_found);
            ToastHelper.showNotify(msg);

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
                        String msg = GlobalData.applicationContext.getString(R.string.bt_connection_failed);

                        throw new Exception(msg);
                    }

                    // Listen to socket
                    BluetoothHelper.startListener(GlobalData.btSocket);

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

    /**
     * Start Listener
     */
    public static void startListener(final BluetoothSocket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        InputStream inStream = null;

                        boolean shouldBreak = !(
                                (socket != null) &&
                                        socket.isConnected() &&
                                        (inStream = socket.getInputStream()) != null
                        );

                        if (shouldBreak) {
                            break;
                        }

                        byte[] data = new byte[1024];
                        int size = inStream.read(data, 0, data.length);

                        if (size > 0) {
                            if (null != events) {
                                byte[] sData = Arrays.copyOfRange(data, 0, size);
                                String strData = new String(sData, Charset.forName("UTF-8"));

                                try {
                                    events.OnCommand(strData);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Send data - String
     *
     * @param cmd
     * @return
     */
    public static boolean send(String cmd) {
        if (null != cmd) {
            byte[] data = new byte[cmd.length()];

            for (int i = 0; i < cmd.length(); ++i) {
                data[i] = (byte) (int) cmd.charAt(i);
            }

            return send(data);
        }

        return false;
    }

    /**
     * Send data
     *
     * @param cmd
     * @return
     */
    public static boolean send(final byte[] cmd) {
        if ((null != cmd) &&
                (null != GlobalData.btSocket) &&
                GlobalData.btSocket.isConnected()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GlobalData.btSocket.getOutputStream()
                                .write(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        return false;
    }
}
