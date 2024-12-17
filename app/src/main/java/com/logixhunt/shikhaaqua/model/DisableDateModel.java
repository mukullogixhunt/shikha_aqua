package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisableDateModel {

    @SerializedName("mn_id")
    @Expose
    private String mnId;
    @SerializedName("mn_date")
    @Expose
    private String mnDate;
    @SerializedName("mn_message")
    @Expose
    private String mnMessage;
    @SerializedName("mn_status")
    @Expose
    private String mnStatus;

    public String getMnId() {
        return mnId;
    }

    public void setMnId(String mnId) {
        this.mnId = mnId;
    }

    public String getMnDate() {
        return mnDate;
    }

    public void setMnDate(String mnDate) {
        this.mnDate = mnDate;
    }

    public String getMnMessage() {
        return mnMessage;
    }

    public void setMnMessage(String mnMessage) {
        this.mnMessage = mnMessage;
    }

    public String getMnStatus() {
        return mnStatus;
    }

    public void setMnStatus(String mnStatus) {
        this.mnStatus = mnStatus;
    }
}
