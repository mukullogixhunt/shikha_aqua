package com.logixhunt.shikhaaqua.model.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "product_cart_master")
public class ProductCart {

    @PrimaryKey
    @NonNull
    @SerializedName("m_bottle_id")
    @Expose
    private String bottleId;
    @SerializedName("m_bottle_company")
    @Expose
    private String bottleCompany;
    @SerializedName("m_bottle_size")
    @Expose
    private String bottleSize;
    @SerializedName("m_bottle_price")
    @Expose
    private String bottlePrice;
    @SerializedName("m_bottle_image")
    @Expose
    private String bottleImage;

    private int itemQuantity = 0;

    public ProductCart() {

    }

    public ProductCart(String bottleId, String bottleCompany, String bottlePrice, String bottleSize, String bottleImage, int itemQuantity) {
        this.bottleId = bottleId;
        this.bottleCompany = bottleCompany;
        this.bottlePrice = bottlePrice;
        this.bottleSize = bottleSize;
        this.bottleImage = bottleImage;
        this.itemQuantity = itemQuantity;
    }

    public String getBottleId() {
        return bottleId;
    }

    public void setBottleId(String bottleId) {
        this.bottleId = bottleId;
    }

    public String getBottleCompany() {
        return bottleCompany;
    }

    public void setBottleCompany(String bottleCompany) {
        this.bottleCompany = bottleCompany;
    }

    public String getBottleSize() {
        return bottleSize;
    }

    public void setBottleSize(String bottleSize) {
        this.bottleSize = bottleSize;
    }

    public String getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(String bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public String getBottleImage() {
        return bottleImage;
    }

    public void setBottleImage(String bottleImage) {
        this.bottleImage = bottleImage;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
