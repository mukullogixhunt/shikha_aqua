package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponModel {

    @SerializedName("m_coupon_id")
    @Expose
    private String mCouponId;
    @SerializedName("m_coupon_code")
    @Expose
    private String mCouponCode;
    @SerializedName("m_coupon_description")
    @Expose
    private String mCouponDescription;
    @SerializedName("m_coupon_min_amt")
    @Expose
    private String mCouponMinAmt;
    @SerializedName("m_coupon_discount")
    @Expose
    private String mCouponDiscount;
    @SerializedName("m_coupon_start_date")
    @Expose
    private String mCouponStartDate;
    @SerializedName("m_coupon_end_date")
    @Expose
    private String mCouponEndDate;

    public String getmCouponId() {
        return mCouponId;
    }

    public void setmCouponId(String mCouponId) {
        this.mCouponId = mCouponId;
    }

    public String getmCouponCode() {
        return mCouponCode;
    }

    public void setmCouponCode(String mCouponCode) {
        this.mCouponCode = mCouponCode;
    }

    public String getmCouponDescription() {
        return mCouponDescription;
    }

    public void setmCouponDescription(String mCouponDescription) {
        this.mCouponDescription = mCouponDescription;
    }

    public String getmCouponMinAmt() {
        return mCouponMinAmt;
    }

    public void setmCouponMinAmt(String mCouponMinAmt) {
        this.mCouponMinAmt = mCouponMinAmt;
    }

    public String getmCouponDiscount() {
        return mCouponDiscount;
    }

    public void setmCouponDiscount(String mCouponDiscount) {
        this.mCouponDiscount = mCouponDiscount;
    }

    public String getmCouponStartDate() {
        return mCouponStartDate;
    }

    public void setmCouponStartDate(String mCouponStartDate) {
        this.mCouponStartDate = mCouponStartDate;
    }

    public String getmCouponEndDate() {
        return mCouponEndDate;
    }

    public void setmCouponEndDate(String mCouponEndDate) {
        this.mCouponEndDate = mCouponEndDate;
    }
}
