package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaModel {

    @SerializedName("m_area_id")
    @Expose
    private String mAreaId;
    @SerializedName("m_area_name")
    @Expose
    private String mAreaName;

    public String getmAreaId() {
        return mAreaId;
    }

    public void setmAreaId(String mAreaId) {
        this.mAreaId = mAreaId;
    }

    public String getmAreaName() {
        return mAreaName;
    }

    public void setmAreaName(String mAreaName) {
        this.mAreaName = mAreaName;
    }
}
