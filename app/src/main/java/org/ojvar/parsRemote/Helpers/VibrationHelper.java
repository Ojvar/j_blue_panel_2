package org.ojvar.parsRemote.Helpers;

import android.content.Context;
import android.os.Vibrator;

import org.ojvar.parsRemote.R;

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
