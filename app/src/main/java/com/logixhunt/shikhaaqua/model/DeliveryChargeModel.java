package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryChargeModel {

    @SerializedName("m_charge_id")
    @Expose
    private String mChargeId;
    @SerializedName("m_charge_city")
    @Expose
    private String mChargeCity;
    @SerializedName("m_charge_price")
    @Expose
    private String mChargePrice;

    public String getmChargeId() {
        return mChargeId;
    }

    public void setmChargeId(String mChargeId) {
        this.mChargeId = mChargeId;
    }

    public String getmChargeCity() {
        return mChargeCity;
    }

    public void setmChargeCity(String mChargeCity) {
        this.mChargeCity = mChargeCity;
    }

    public String getmChargePrice() {
        return mChargePrice;
    }

    public void setmChargePrice(String mChargePrice) {
        this.mChargePrice = mChargePrice;
    }
}
