package org.ojvar.parsRemote.Helpers;

import android.content.Context;
import android.widget.Toast;

import org.ojvar.parsRemote.App.GlobalData;

public class ToastHelper {
    private static Toast toaster;

    /**
     * Show toast notify
     *
     * @param message
     * @return
     */
    public static Toast showNotify(String message, Context context) {
        hideNotify();

        toaster = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toaster.show();

        return toaster;
    }

    /**
     * Hide notify
     */
    public static void hideNotify() {
        if (null != toaster) {
            toaster.cancel();
        }
    }
}
