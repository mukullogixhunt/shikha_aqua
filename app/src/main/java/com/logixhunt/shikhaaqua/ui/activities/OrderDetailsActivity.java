package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.OrderResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityOrderDetailsBinding;
import com.logixhunt.shikhaaqua.model.OrderModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.adapters.OrderItemAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseActivity {

    private ActivityOrderDetailsBinding binding;
    private UserModel userModel = new UserModel();
    private OrderModel orderModel = new OrderModel();
    private OrderItemAdapter orderItemAdapter;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        userModel = getUserData(OrderDetailsActivity.this);
        orderId = getIntent().getStringExtra(Constant.BundleExtras.ORDER_ID);
    }

    private void initialization() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.cardCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + orderModel.getmExecutiveMobile()));
                startActivity(intent);
            }
        });

        getOrderDetails();
    }

    private void getOrderDetails() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiService.getOrderDetails(orderId);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            orderModel = response.body().getOrders().get(0);

                            setBookingData();
                        } else {

                        }
                    } else {

                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);

                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void setBookingData() {

        switch (orderModel.getmOrderStatus()) {
            case "1":
                binding.ivPending.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setVisibility(View.VISIBLE);
                binding.ivRunning.setVisibility(View.VISIBLE);
                binding.tvDispatched.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
                binding.ivCompleted.setVisibility(View.VISIBLE);
                binding.tvDelivered.setVisibility(View.VISIBLE);
                binding.servicePartnerCard.setVisibility(View.GONE);
                binding.cancelledCard.setVisibility(View.GONE);
                break;
            case "2":
                binding.view1.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivAccepted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setVisibility(View.VISIBLE);
                binding.ivRunning.setVisibility(View.VISIBLE);
                binding.tvDispatched.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
                binding.ivCompleted.setVisibility(View.VISIBLE);
                binding.tvDelivered.setVisibility(View.VISIBLE);
                binding.servicePartnerCard.setVisibility(View.VISIBLE);
                binding.cancelledCard.setVisibility(View.GONE);
                break;
            case "3":
                binding.view1.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivAccepted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivRunning.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setVisibility(View.VISIBLE);
                binding.ivRunning.setVisibility(View.VISIBLE);
                binding.tvDispatched.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
                binding.ivCompleted.setVisibility(View.VISIBLE);
                binding.tvDelivered.setVisibility(View.VISIBLE);
                binding.servicePartnerCard.setVisibility(View.VISIBLE);
                binding.cancelledCard.setVisibility(View.GONE);
                break;
            case "4":
                binding.view1.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivAccepted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivRunning.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view3.setBackgroundColor(getResources().getColor(R.color.primary));
                binding.ivCompleted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
                binding.view2.setVisibility(View.VISIBLE);
                binding.ivRunning.setVisibility(View.VISIBLE);
                binding.tvDispatched.setVisibility(View.VISIBLE);
                binding.view3.setVisibility(View.VISIBLE);
                binding.ivCompleted.setVisibility(View.VISIBLE);
                binding.tvDelivered.setVisibility(View.VISIBLE);
                binding.servicePartnerCard.setVisibility(View.VISIBLE);
                binding.cancelledCard.setVisibility(View.GONE);
                break;
            case "5":
                binding.ivPending.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.error));
                binding.view1.setBackgroundColor(getColor(R.color.error));
                binding.ivAccepted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.error));
                binding.view2.setBackgroundColor(getColor(R.color.error));
                binding.ivRunning.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.error));
                binding.view3.setBackgroundColor(getColor(R.color.error));
                binding.ivCompleted.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.error));
                binding.tvDelivered.setText(R.string.rejected);

                binding.cancelledCard.setVisibility(View.VISIBLE);
                binding.servicePartnerCard.setVisibility(View.GONE);

                if (orderModel.getmOrderRejectReason().isEmpty()) {
                    binding.tvReason.setText("Unknown");
                } else {
                    binding.tvReason.setText(orderModel.getmOrderRejectReason());
                }

                break;

        }

        binding.orderId.setText(String.format("#%s", orderModel.getmOrderBillNo()));

        binding.tvTime.setText(Utils.changeDateFormat(Constant.HHmmss, Constant.hhmma, orderModel.getmOrderTime()));
        binding.tvDate.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.EEEEddMMMM, orderModel.getmOrderDate()));


        binding.tvDeliveryTime.setText(orderModel.getmOrderDelvTime());

        if (orderModel.getmOrderDeliveredDate().equals("0000-00-00 00:00:00")) {
            binding.tvDeliveryDate.setText("Not Delivered");
        } else {
            binding.tvDeliveryDate.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.EEEEddMMMM, orderModel.getmOrderDeliveredDate()));
        }


        binding.tvDeliveryDateSlot.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.EEEEddMMMM, orderModel.getmOrderDelvDate()));

        binding.itemTotal.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(orderModel.getmOrderSubTotal())));
        binding.tvDeliveryCharge.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(orderModel.getmOrderDelvCharges())));


        binding.tvServicePartnerName.setText(orderModel.getmExecutiveName());


        binding.couponDiscount.setText(String.format("-%s/-", new IndianCurrencyFormat().inCuFormatText(orderModel.getmOrderCouponDiscount())));


        binding.grandTotal.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(orderModel.getmOrderGTotal())));
        binding.tvDeliveryCharge.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(orderModel.getmOrderDelvCharges())));

        binding.payMode.setText(orderModel.getmOrderPaymode());


        binding.rvItems.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
        orderItemAdapter = new OrderItemAdapter(orderModel.getOrderItems(), this);
        binding.rvItems.setAdapter(orderItemAdapter);


    }

}