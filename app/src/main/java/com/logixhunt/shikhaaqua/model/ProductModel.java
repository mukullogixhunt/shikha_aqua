package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel {

    @SerializedName("m_bottle_id")
    @Expose
    private String mBottleId;
    @SerializedName("m_bottle_company")
    @Expose
    private String mBottleCompany;
    @SerializedName("m_bottle_size")
    @Expose
    private String mBottleSize;
    @SerializedName("m_bottle_price")
    @Expose
    private String mBottlePrice;
    @SerializedName("m_bottle_image")
    @Expose
    private String mBottleImage;

    private int itemQuantity = 0;

    public String getmBottleId() {
        return mBottleId;
    }

    public void setmBottleId(String mBottleId) {
        this.mBottleId = mBottleId;
    }

    public String getmBottleCompany() {
        return mBottleCompany;
    }

    public void setmBottleCompany(String mBottleCompany) {
        this.mBottleCompany = mBottleCompany;
    }

    public String getmBottleSize() {
        return mBottleSize;
    }

    public void setmBottleSize(String mBottleSize) {
        this.mBottleSize = mBottleSize;
    }

    public String getmBottlePrice() {
        return mBottlePrice;
    }

    public void setmBottlePrice(String mBottlePrice) {
        this.mBottlePrice = mBottlePrice;
    }

    public String getmBottleImage() {
        return mBottleImage;
    }

    public void setmBottleImage(String mBottleImage) {
        this.mBottleImage = mBottleImage;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
