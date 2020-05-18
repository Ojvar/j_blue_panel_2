package org.ojvar.bluepanel2.Helpers;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.R;

public class SecurityHelper {
    /**
     * Attempt
     *
     * @param password  User password
     * @return true/false
     */
    public static boolean attempt(String password) {
        String pwd = GlobalData.applicationContext.getString(R.string.pwd);

        return password.equals(pwd);
    }
}
