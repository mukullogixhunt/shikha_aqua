package com.logixhunt.shikhaaqua.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.CouponModel;
import com.logixhunt.shikhaaqua.model.DeliveryChargeModel;

import java.util.List;

public class CouponResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<CouponModel> coupons;

    public List<CouponModel> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponModel> coupons) {
        this.coupons = coupons;
    }
}
