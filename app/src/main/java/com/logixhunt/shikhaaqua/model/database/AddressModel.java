package com.logixhunt.shikhaaqua.model.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "address_master")
public class AddressModel {

    @PrimaryKey(autoGenerate = true)
    private int addressId;
    private String AddressTitle = "";
    private String pinCode = "";
    private String address = "";
    private String lat = "";
    private String lng = "";

    public AddressModel(String AddressTitle, String pinCode, String address, String lat, String lng) {
        this.AddressTitle = AddressTitle;
        this.pinCode = pinCode;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressTitle() {
        return AddressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.AddressTitle = addressTitle;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
