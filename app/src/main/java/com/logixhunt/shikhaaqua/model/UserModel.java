package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("m_user_id")
    @Expose
    private String mUserId;
    @SerializedName("m_user_name")
    @Expose
    private String mUserName;
    @SerializedName("m_user_mobile")
    @Expose
    private String mUserMobile;
    @SerializedName("m_user_email")
    @Expose
    private String mUserEmail;
    @SerializedName("m_user_gender")
    @Expose
    private String mUserGender;
    @SerializedName("m_user_dob")
    @Expose
    private String mUserDob;
    @SerializedName("m_state_id")
    @Expose
    private String mStateId;
    @SerializedName("m_state_name")
    @Expose
    private String mStateName;
    @SerializedName("m_city_id")
    @Expose
    private String mCityId;
    @SerializedName("m_city_name")
    @Expose
    private String mCityName;
    @SerializedName("m_user_address")
    @Expose
    private String mUserAddress;

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserMobile() {
        return mUserMobile;
    }

    public void setmUserMobile(String mUserMobile) {
        this.mUserMobile = mUserMobile;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public String getmUserGender() {
        return mUserGender;
    }

    public void setmUserGender(String mUserGender) {
        this.mUserGender = mUserGender;
    }

    public String getmUserDob() {
        return mUserDob;
    }

    public void setmUserDob(String mUserDob) {
        this.mUserDob = mUserDob;
    }

    public String getmStateId() {
        return mStateId;
    }

    public void setmStateId(String mStateId) {
        this.mStateId = mStateId;
    }

    public String getmStateName() {
        return mStateName;
    }

    public void setmStateName(String mStateName) {
        this.mStateName = mStateName;
    }

    public String getmCityId() {
        return mCityId;
    }

    public void setmCityId(String mCityId) {
        this.mCityId = mCityId;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmUserAddress() {
        return mUserAddress;
    }

    public void setmUserAddress(String mUserAddress) {
        this.mUserAddress = mUserAddress;
    }
}
