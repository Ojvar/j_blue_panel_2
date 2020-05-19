package org.ojvar.bluepanel2.Models;

import com.google.gson.Gson;

public class SettingModel {
    private String password = "";
    private String deviceId = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * To Json
     *
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    /**
     * From json
     *
     * @return
     */
    public void praseJson(String json) {
        Gson gson = new Gson();

        SettingModel model = gson.fromJson(json, SettingModel.class);

        this.deviceId = model.deviceId;
        this.password = model.password;
    }
}
