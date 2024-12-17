package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("notification_id")
    @Expose
    private String notificationId;
    @SerializedName("notification_text")
    @Expose
    private String notificationText;
    @SerializedName("notification_desc")
    @Expose
    private String notificationDesc;
    @SerializedName("nofification_date")
    @Expose
    private String nofificationDate;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getNofificationDate() {
        return nofificationDate;
    }

    public void setNofificationDate(String nofificationDate) {
        this.nofificationDate = nofificationDate;
    }
}
