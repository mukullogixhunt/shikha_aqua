package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.model.NotificationModel;

import java.util.List;

public class NotificationResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<NotificationModel> notifications;

    public List<NotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
}
