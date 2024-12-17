package com.logixhunt.shikhaaqua.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;

public class PaymentResponse extends BaseResponse {

    @SerializedName("m_trans_id")
    @Expose
    private Double mTransId;

    public Double getmTransId() {
        return mTransId;
    }

    public void setmTransId(Double mTransId) {
        this.mTransId = mTransId;
    }
}
