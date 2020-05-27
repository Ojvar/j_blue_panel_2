package org.ojvar.bluepanel2.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

import org.ojvar.bluepanel2.App.GlobalData;

public class ProgressHelper {
    private static ProgressDialog dialog = null;

    /**
     * Show Progress
     *
     * @param title
     * @param message
     */
    public static void showProgress(Context context, String title, String message) {
        hideProgress();

        dialog = ProgressDialog.show(context, title, message, true);
    }

    /**
     * Hide progress
     */
    public static void hideProgress() {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
