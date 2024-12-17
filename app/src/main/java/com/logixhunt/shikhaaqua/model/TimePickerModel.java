package com.logixhunt.shikhaaqua.model;

public class TimePickerModel {

    private String fromTime;
    private String toTime;
    private String price;

    public TimePickerModel(String fromTime, String toTime, String price) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.price = price;
    }

    public TimePickerModel() {

    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
