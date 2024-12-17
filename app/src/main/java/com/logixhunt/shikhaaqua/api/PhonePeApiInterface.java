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
import com.logixhunt.shikhaaqua.api.response.phonepe.PhonePeResponse;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhonePeApiInterface {


    //Phone PE
    @GET("apis/hermes/pg/v1/status/{merchantId}/{merchantTransactionId}")
    Call<PhonePeResponse> checkStatus(
            @Path("merchantId") String merchantId,
            @Path("merchantTransactionId") String merchantTransactionId,
            @HeaderMap Map<String,String> headers);

}
