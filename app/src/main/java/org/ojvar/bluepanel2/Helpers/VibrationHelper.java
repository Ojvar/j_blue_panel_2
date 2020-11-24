package org.ojvar.bluepanel2.Helpers;

import android.content.Context;
import android.os.Vibrator;

import org.ojvar.bluepanel2.R;

/**
 * Virbration helper
 */
public class VibrationHelper {
    /**
     * Make vibration
     *
     * @param miliseconds
     */
    public static void vibrate(Context context, int miliseconds) {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (null != vibe) {
            vibe.vibrate(miliseconds);
        }
    }

    /**
     * Make vibration
     */
    public static void vibrate(Context context) {
        vibrate(context, context.getResources().getInteger(R.integer.VIBRATE_TIME));
    }
}
