package com.logixhunt.shikhaaqua.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.DeliveryChargeModel;
import com.logixhunt.shikhaaqua.model.UserModel;

import java.util.List;

public class DeliveryChargeResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<DeliveryChargeModel> deliveryCharges;

    public List<DeliveryChargeModel> getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(List<DeliveryChargeModel> deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }
}
