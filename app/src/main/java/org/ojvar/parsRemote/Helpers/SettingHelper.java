package org.ojvar.parsRemote.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import org.ojvar.parsRemote.App.GlobalData;

public class SettingHelper {
    public static final String KEY_PREFERENCES = "preferences";
    public static final String KEY_SETTINGS = "settings";
    public static final String KEY_DATA_MODEL = "data_model";

    /**
     * Save setting
     */
    public static void saveSetting() {
        SharedPreferences.Editor editor = GlobalData.applicationContext.getSharedPreferences(
                KEY_PREFERENCES,
                Context.MODE_PRIVATE).edit();

        editor.putString(KEY_SETTINGS, GlobalData.settings.toJson());
        editor.putString(KEY_DATA_MODEL, GlobalData.dataModel.toJson());

        editor.commit();
    }

    /**
     * Load setting
     */
    public static void loadSetting() {
        SharedPreferences preferences =
                GlobalData.applicationContext.getSharedPreferences(
                        KEY_PREFERENCES,
                        Context.MODE_PRIVATE);

        String settings = preferences.getString(KEY_SETTINGS, "{}");
        GlobalData.settings.praseJson(settings);
        
        String dataModel = preferences.getString(KEY_DATA_MODEL, "{}");
        GlobalData.dataModel.praseJson(dataModel);
    }
}
