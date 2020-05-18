package org.ojvar.bluepanel2.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import org.ojvar.bluepanel2.App.GlobalData;

public class SettingHelper {
    public static final String KEY_PREFERENCES = "preferences";
    public static final String KEY_SETTINGS = "settings";

    /**
     * Save setting
     */
    public static void saveSetting() {
        SharedPreferences.Editor editor =
                (SharedPreferences.Editor) GlobalData.applicationContext.getSharedPreferences(
                        KEY_PREFERENCES,
                        Context.MODE_PRIVATE);

        editor.putString(KEY_SETTINGS, GlobalData.settings.toJson());

        editor.apply();
        editor.commit();
    }

    /**
     * Load setting
     */
    public static void loadSetting() {
        SharedPreferences preferences =
                GlobalData.applicationContext.getSharedPreferences(
                        KEY_SETTINGS,
                        Context.MODE_PRIVATE);

        String settings = preferences.getString("settings", "{}");
        GlobalData.settings.praseJson(settings);
    }
}
