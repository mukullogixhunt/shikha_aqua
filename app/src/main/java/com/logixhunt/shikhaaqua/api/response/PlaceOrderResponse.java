package com.logixhunt.shikhaaqua.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;

public class PlaceOrderResponse extends BaseResponse {

    @SerializedName("m_order_id")
    @Expose
    private Integer mOrderId;
    @SerializedName("m_order_bill_no")
    @Expose
    private String mOrderBillNo;

    public Integer getmOrderId() {
        return mOrderId;
    }

    public void setmOrderId(Integer mOrderId) {
        this.mOrderId = mOrderId;
    }

    public String getmOrderBillNo() {
        return mOrderBillNo;
    }

    public void setmOrderBillNo(String mOrderBillNo) {
        this.mOrderBillNo = mOrderBillNo;
    }
}
