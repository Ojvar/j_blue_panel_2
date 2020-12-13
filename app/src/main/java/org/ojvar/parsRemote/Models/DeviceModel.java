package org.ojvar.parsRemote.Models;

import androidx.annotation.NonNull;

public class DeviceModel {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Ctr
     */
    public DeviceModel() {
    }

    /**
     * Ctr
     */
    public DeviceModel(String name, String id) {
        this.id = id;
        this.name = name;
    }

    /**
     * To String
     *
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return ("" + name);
    }
}
