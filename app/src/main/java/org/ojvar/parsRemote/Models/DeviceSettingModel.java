package org.ojvar.parsRemote.Models;

/**
 * Device setting model
 */
public class DeviceSettingModel {
    private int id;
    private String title;
    private String description;
    private ValueRange range;
    private int value;
    private boolean readOnly;

    /**
     * Ctr
     *
     * @param id
     * @param title
     * @param description
     * @param range
     */
    public DeviceSettingModel(int id, String title, String description, ValueRange range, int value, boolean readOnly) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.range = range;
        this.value = value;
        this.readOnly = readOnly;
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

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Value range
     */
    public class ValueRange {
        public int min;
        public int max;
    }
}
