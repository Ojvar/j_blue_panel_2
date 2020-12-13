package org.ojvar.parsRemote.Helpers;

import org.ojvar.parsRemote.App.GlobalData;

public class SecurityHelper {
    /**
     * Attempt
     *
     * @param password  User password
     * @return true/false
     */
    public static boolean attempt(String password) {
        String pwd = GlobalData.settings.getPassword();

        pwd += "";
        return password.equals(pwd);
    }
}
