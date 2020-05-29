package org.ojvar.bluepanel2.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

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
            try {
                dialog.dismiss();
            } catch (Exception ex) {
            }

            dialog = null;
        }
    }
}
