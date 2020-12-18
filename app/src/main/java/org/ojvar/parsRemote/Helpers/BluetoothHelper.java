package org.ojvar.parsRemote.Helpers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import org.ojvar.parsRemote.App.GlobalData;
import org.ojvar.parsRemote.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BluetoothHelper {
    public static BluetoothEvents events;
    private static String _buffer = "";

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
    public static boolean init(Context context) {
        GlobalData.btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (GlobalData.btAdapter == null) {
            String msg = GlobalData.applicationContext.getString(R.string.bluetooth_not_found);
            ToastHelper.showNotify(msg, context);

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
    public static boolean connect(String deviceId, final BluetoothEvents events, Context context) {
        if (null == GlobalData.btAdapter) {
            return false;
        }

        BluetoothHelper.events = events;

        GlobalData.btDevice = GlobalData.btAdapter.getRemoteDevice(deviceId);
        if (GlobalData.btDevice == null) {
            String msg = GlobalData.applicationContext.getString(R.string.device_not_found);
            ToastHelper.showNotify(msg, context);

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
                                    updateInputData(strData);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (null != events) {
                            events.OnDisconnect();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Update input data
     *
     * @param strData
     */
    private static synchronized void updateInputData(String strData) {
        /* Append to buffer */
        _buffer += strData;

        _buffer = _buffer.replaceAll("\r", "\n").replaceAll("\n\n", "\n");

        /* Create chunks */
        int pos = -1;
        do {
            pos = _buffer.indexOf("\n");

            if (pos == -1) {
                break;
            }

            /* Split string */
            String strToSend = _buffer.substring(0, pos);
            events.OnCommand(strToSend);

            /* Update _buffer */
            _buffer = _buffer.substring(pos+1);
        } while (true);
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
                        GlobalData.btSocket.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        return false;
    }
}
