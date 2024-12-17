package com.logixhunt.shikhaaqua.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel {


    @SerializedName("m_order_id")
    @Expose
    private String mOrderId;
    @SerializedName("m_order_bill_no")
    @Expose
    private String mOrderBillNo;
    @SerializedName("m_order_date")
    @Expose
    private String mOrderDate;
    @SerializedName("m_order_time")
    @Expose
    private String mOrderTime;
    @SerializedName("m_order_user")
    @Expose
    private String mOrderUser;
    @SerializedName("m_order_g_total")
    @Expose
    private String mOrderGTotal;
    @SerializedName("m_order_lat")
    @Expose
    private String mOrderLat;
    @SerializedName("m_order_long")
    @Expose
    private String mOrderLong;
    @SerializedName("m_order_address")
    @Expose
    private String mOrderAddress;
    @SerializedName("m_order_delv_date")
    @Expose
    private String mOrderDelvDate;
    @SerializedName("m_order_delivered_date")
    @Expose
    private String mOrderDeliveredDate;
    @SerializedName("m_order_delv_time")
    @Expose
    private String mOrderDelvTime;
    @SerializedName("m_order_status")
    @Expose
    private String mOrderStatus;
    @SerializedName("m_order_sub_total")
    @Expose
    private String mOrderSubTotal;
    @SerializedName("m_order_delv_charges")
    @Expose
    private String mOrderDelvCharges;
    @SerializedName("m_order_coupon_discount")
    @Expose
    private String mOrderCouponDiscount;
    @SerializedName("m_coupon_code")
    @Expose
    private String mCouponCode;
    @SerializedName("m_order_paymode")
    @Expose
    private String mOrderPaymode;

    @SerializedName("m_executive_id")
    @Expose
    private String mExecutiveId;
    @SerializedName("m_executive_name")
    @Expose
    private String mExecutiveName;
    @SerializedName("m_executive_mobile")
    @Expose
    private String mExecutiveMobile;

    @SerializedName("m_order_reject_reason")
    @Expose
    private String mOrderRejectReason;


    @SerializedName("order_items")
    @Expose
    private List<OrderItemModel> orderItems;

    public String getmOrderId() {
        return mOrderId;
    }

    public void setmOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public String getmOrderBillNo() {
        return mOrderBillNo;
    }

    public void setmOrderBillNo(String mOrderBillNo) {
        this.mOrderBillNo = mOrderBillNo;
    }

    public String getmOrderDate() {
        return mOrderDate;
    }

    public void setmOrderDate(String mOrderDate) {
        this.mOrderDate = mOrderDate;
    }

    public String getmOrderTime() {
        return mOrderTime;
    }

    public void setmOrderTime(String mOrderTime) {
        this.mOrderTime = mOrderTime;
    }

    public String getmOrderUser() {
        return mOrderUser;
    }

    public void setmOrderUser(String mOrderUser) {
        this.mOrderUser = mOrderUser;
    }

    public String getmOrderGTotal() {
        return mOrderGTotal;
    }

    public void setmOrderGTotal(String mOrderGTotal) {
        this.mOrderGTotal = mOrderGTotal;
    }

    public String getmOrderLat() {
        return mOrderLat;
    }

    public void setmOrderLat(String mOrderLat) {
        this.mOrderLat = mOrderLat;
    }

    public String getmOrderLong() {
        return mOrderLong;
    }

    public void setmOrderLong(String mOrderLong) {
        this.mOrderLong = mOrderLong;
    }

    public String getmOrderAddress() {
        return mOrderAddress;
    }

    public void setmOrderAddress(String mOrderAddress) {
        this.mOrderAddress = mOrderAddress;
    }

    public String getmOrderDelvDate() {
        return mOrderDelvDate;
    }

    public void setmOrderDelvDate(String mOrderDelvDate) {
        this.mOrderDelvDate = mOrderDelvDate;
    }

    public String getmOrderDelvTime() {
        return mOrderDelvTime;
    }

    public void setmOrderDelvTime(String mOrderDelvTime) {
        this.mOrderDelvTime = mOrderDelvTime;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public void setmOrderStatus(String mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    public String getmOrderPaymode() {
        return mOrderPaymode;
    }

    public void setmOrderPaymode(String mOrderPaymode) {
        this.mOrderPaymode = mOrderPaymode;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public String getmOrderSubTotal() {
        return mOrderSubTotal;
    }

    public void setmOrderSubTotal(String mOrderSubTotal) {
        this.mOrderSubTotal = mOrderSubTotal;
    }

    public String getmOrderDelvCharges() {
        return mOrderDelvCharges;
    }

    public void setmOrderDelvCharges(String mOrderDelvCharges) {
        this.mOrderDelvCharges = mOrderDelvCharges;
    }

    public String getmOrderCouponDiscount() {
        return mOrderCouponDiscount;
    }

    public void setmOrderCouponDiscount(String mOrderCouponDiscount) {
        this.mOrderCouponDiscount = mOrderCouponDiscount;
    }

    public String getmCouponCode() {
        return mCouponCode;
    }

    public void setmCouponCode(String mCouponCode) {
        this.mCouponCode = mCouponCode;
    }

    public String getmExecutiveId() {
        return mExecutiveId;
    }

    public void setmExecutiveId(String mExecutiveId) {
        this.mExecutiveId = mExecutiveId;
    }

    public String getmExecutiveName() {
        return mExecutiveName;
    }

    public void setmExecutiveName(String mExecutiveName) {
        this.mExecutiveName = mExecutiveName;
    }

    public String getmExecutiveMobile() {
        return mExecutiveMobile;
    }

    public void setmExecutiveMobile(String mExecutiveMobile) {
        this.mExecutiveMobile = mExecutiveMobile;
    }

    public String getmOrderRejectReason() {
        return mOrderRejectReason;
    }

    public void setmOrderRejectReason(String mOrderRejectReason) {
        this.mOrderRejectReason = mOrderRejectReason;
    }

    public String getmOrderDeliveredDate() {
        return mOrderDeliveredDate;
    }

    public void setmOrderDeliveredDate(String mOrderDeliveredDate) {
        this.mOrderDeliveredDate = mOrderDeliveredDate;
    }
}
