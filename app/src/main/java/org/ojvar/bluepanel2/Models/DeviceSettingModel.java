package org.ojvar.bluepanel2.Models;

import com.google.gson.Gson;

/**
 * Device setting model
 */
public class DeviceSettingModel {
    private int id;
    private String title;
    private String description;
    private ValueRange range;
    private int value;

    /**
     * Ctr
     *
     * @param id
     * @param title
     * @param description
     * @param range
     */
    public DeviceSettingModel(int id, String title, String description, ValueRange range, int value) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.range = range;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ValueRange getRange() {
        return range;
    }

    public void setRange(ValueRange range) {
        this.range = range;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Value range
     */
    public class ValueRange {
        public int min;
        public int max;
    }
}
