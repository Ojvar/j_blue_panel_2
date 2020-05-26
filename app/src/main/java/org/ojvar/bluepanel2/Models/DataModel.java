package org.ojvar.bluepanel2.Models;

import com.google.gson.Gson;

import java.util.HashMap;

public class DataModel {
    private HashMap<String, String> data = new HashMap<>();

    /**
     * Set value
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        data.put(key, value);
    }

    /**
     * Set value
     *
     * @param key
     */
    public String getValue(String key, String defaultValue) {
        if (data.containsKey(key)) {
            return data.get(key);
        }

        return defaultValue;
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

        DataModel model = gson.fromJson(json, DataModel.class);

        this.data = model.data;
    }
}
