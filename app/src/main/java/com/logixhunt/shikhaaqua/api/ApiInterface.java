package com.logixhunt.shikhaaqua.api;


import com.logixhunt.shikhaaqua.api.response.AreaResponse;
import com.logixhunt.shikhaaqua.api.response.BalanceResponse;
import com.logixhunt.shikhaaqua.api.response.CityResponse;
import com.logixhunt.shikhaaqua.api.response.CouponResponse;
import com.logixhunt.shikhaaqua.api.response.DeliveryChargeResponse;
import com.logixhunt.shikhaaqua.api.response.DisableDateResponse;
import com.logixhunt.shikhaaqua.api.response.NotificationResponse;
import com.logixhunt.shikhaaqua.api.response.OrderResponse;
import com.logixhunt.shikhaaqua.api.response.PaymentResponse;
import com.logixhunt.shikhaaqua.api.response.PlaceOrderResponse;
import com.logixhunt.shikhaaqua.api.response.ProductResponse;
import com.logixhunt.shikhaaqua.api.response.SliderResponse;
import com.logixhunt.shikhaaqua.api.response.StateResponse;
import com.logixhunt.shikhaaqua.api.response.UserResponse;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(Constant.EndPoint.USER_LOGIN)
    Call<UserResponse> userLogin(
            @Field(Constant.ApiKey.M_USER_MOBILE) String m_user_mobile
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.VERIFY_USER_OTP)
    Call<UserResponse> verifyOtp(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_USER_OTP) String m_user_otp
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.SEND_USER_OTP)
    Call<UserResponse> sendOtp(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id
    );

    @POST(Constant.EndPoint.GET_STATE)
    Call<StateResponse> getState();

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_CITY)
    Call<CityResponse> getCity(
            @Field(Constant.ApiKey.M_STATE_ID) String m_state_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_AREA)
    Call<AreaResponse> getArea(
            @Field(Constant.ApiKey.M_CITY_ID) String m_city_id
    );

    @POST(Constant.EndPoint.GET_SLIDERS)
    Call<SliderResponse> getSlider();


    @POST(Constant.EndPoint.GET_BOTTLES)
    Call<ProductResponse> getBottles();


    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_USER_PROFILE)
    Call<UserResponse> updateUserProfile(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_USER_NAME) String m_user_name,
            @Field(Constant.ApiKey.M_USER_EMAIL) String m_user_email,
            @Field(Constant.ApiKey.M_USER_DOB) String m_user_dob,
            @Field(Constant.ApiKey.M_USER_STATE) String m_user_state,
            @Field(Constant.ApiKey.M_USER_CITY) String m_user_city,
            @Field(Constant.ApiKey.M_USER_ADDRESS) String m_user_address
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_ORDER)
    Call<PlaceOrderResponse> placeOrder(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_ORDER_LAT) String m_order_lat,
            @Field(Constant.ApiKey.M_ORDER_LONG) String m_order_long,
            @Field(Constant.ApiKey.M_ORDER_AREA) String m_order_area,
            @Field(Constant.ApiKey.M_ORDER_ADDRESS) String m_order_address,
            @Field(Constant.ApiKey.M_ORDER_OTHER_ADDRESS) String m_order_other_address ,
            @Field(Constant.ApiKey.M_ORDER_DELV_DATE) String m_order_delv_date,
            @Field(Constant.ApiKey.M_ORDER_DELV_TIME) String m_order_delv_time,
            @Field(Constant.ApiKey.M_ORDER_DELV_CHARGES) String m_order_delv_charges,
            @Field(Constant.ApiKey.M_ORDER_COUPON_ID) String m_order_coupon_id,
            @Field(Constant.ApiKey.M_ORDER_COUPON_DISCOUNT) String m_order_coupon_discount,
            @Field(Constant.ApiKey.M_ORDER_G_TOTAL) String m_order_g_total,
            @Field(Constant.ApiKey.M_ORDER_PAYMODE) String m_order_paymode,
            @Field(Constant.ApiKey.M_ORDER_REMARK) String m_order_remark,
            @FieldMap HashMap<String, String> m_bottle_id,
            @FieldMap HashMap<String, String> m_bottle_qty,
            @FieldMap HashMap<String, String> m_bottle_price

    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.ORDER_LIST)
    Call<OrderResponse> getOrder(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_ORDER_STATUS) String m_order_status
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.RECENT_ORDER_LIST)
    Call<OrderResponse> getRecentOrder(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.ORDER_DETAILS)
    Call<OrderResponse> getOrderDetails(
            @Field(Constant.ApiKey.M_ORDER_ID) String m_order_id
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_FCM)
    Call<BaseResponse> updateFcm(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_USER_FCM) String m_user_fcm
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_NOTIFICATION)
    Call<NotificationResponse> getNotifications(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_DELIVERY_CHARGES)
    Call<DeliveryChargeResponse> getDeliveryCharges(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id,
            @Field(Constant.ApiKey.M_CITY_ID) String m_city_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.CHECK_USER_ACTIVITY)
    Call<BaseResponse> checkUsers(
            @Field(Constant.ApiKey.M_USER_MOBILE) String m_user_mobile
    );

    @POST(Constant.EndPoint.NO_ORDER_DATE)
    Call<DisableDateResponse> disabledDate();

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_ALL_COUPON_LIST)
    Call<CouponResponse> getCoupons(
            @Field(Constant.ApiKey.M_USER_ID) String m_user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.GET_COUPON)
    Call<CouponResponse> applyCouponCode(
            @Field(Constant.ApiKey.M_USER_ID) String user_id,
            @Field(Constant.ApiKey.M_COUPON_CODE) String m_coupon_code
    );


    @FormUrlEncoded
    @POST(Constant.EndPoint.PAYMENT_BALANCE)
    Call<BalanceResponse> getPaymentBalance(
            @Field(Constant.ApiKey.M_USER_ID) String user_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.INSERT_PAYMENT)
    Call<PaymentResponse> insertPayment(
            @Field(Constant.ApiKey.M_USER_ID) String user_id,
            @Field(Constant.ApiKey.M_PAID_AMOUNT) String m_paid_amount,
            @Field(Constant.ApiKey.M_TRANSACTION_ID) String m_transaction_id
    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_ORDER_PAYMENT_STATUS)
    Call<BaseResponse> updateOrderPaymentStatus(
            @Field(Constant.ApiKey.M_ORDER_BILL_NO) String m_order_bill_no,
            @Field(Constant.ApiKey.M_TRANSACTION_NUMBER) String m_transaction_number,
            @Field(Constant.ApiKey.M_PAYMENT_STATUS) String m_payment_status

    );

    @FormUrlEncoded
    @POST(Constant.EndPoint.UPDATE_PAYMENT_STATUS)
    Call<BaseResponse> updatePaymentStatus(
            @Field(Constant.ApiKey.M_TRANS_ID) String m_trans_id,
            @Field(Constant.ApiKey.M_PAYMENT_ID) String m_payment_id,
            @Field(Constant.ApiKey.M_TRANS_STATUS) String m_trans_status
    );


}
