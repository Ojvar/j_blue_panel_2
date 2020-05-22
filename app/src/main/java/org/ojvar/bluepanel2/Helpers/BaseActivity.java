package org.ojvar.bluepanel2.Helpers;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static org.ojvar.bluepanel2.App.GlobalData.clearBTEventHandler;

public class BaseActivity extends AppCompatActivity {
    /**
     * On Pause
     */
    @Override
    protected void onPause() {
        super.onPause();

        clearBTEventHandler();
    }

    /**
     * @return "[package]:id/[xml-id]"
     * where [package] is your package and [xml-id] is id of view
     * or "no-id" if there is no id
     */
    public int getId(String name) {
        int resId = this.getResources().getIdentifier(name, "id", this.getPackageName());

        return resId;
    }

    /**
     * Find view by name
     *
     * @param name
     * @return
     */
    public View findViewByName(String name) {
        int resId = getId(name);

        if (resId == 0) {
            return null;
        }

        return findViewById(resId);
    }
}
