package com.logixhunt.shikhaaqua.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.model.DeliveryChargeModel;
import com.logixhunt.shikhaaqua.model.DisableDateModel;

import java.util.List;

public class DisableDateResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<DisableDateModel> disabledDates;

    public List<DisableDateModel> getDisabledDates() {
        return disabledDates;
    }

    public void setDisabledDates(List<DisableDateModel> disabledDates) {
        this.disabledDates = disabledDates;
    }
}
