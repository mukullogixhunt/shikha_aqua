package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.CouponResponse;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.ActivityCouponsBinding;
import com.logixhunt.shikhaaqua.listeners.RequestCouponClickListener;
import com.logixhunt.shikhaaqua.model.CouponModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;
import com.logixhunt.shikhaaqua.ui.adapters.CouponAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsActivity extends BaseActivity implements RequestCouponClickListener {

    private ActivityCouponsBinding binding;
    private UserModel userModel = new UserModel();

    private List<CouponModel> couponList = new ArrayList<>();
    private CouponAdapter couponAdapter;
    private List<ProductCart> cartList = new ArrayList<>();
    private DataService dataService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCouponsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        userModel = getUserData(CouponsActivity.this);
    }

    private void initialization() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dataService = new DataService(CouponsActivity.this);
        cartList.clear();
        cartList = dataService.getAllCartItem();

        binding.rvCoupons.setLayoutManager(new LinearLayoutManager(CouponsActivity.this));
        couponAdapter = new CouponAdapter(CouponsActivity.this, couponList, this);
        binding.rvCoupons.setAdapter(couponAdapter);

        binding.tvCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.tvApply.setTextColor(getColor(R.color.primary));
                    binding.tvApply.setEnabled(true);
                } else {
                    binding.tvApply.setTextColor(getColor(R.color.outline));
                    binding.tvApply.setEnabled(false);
                }
            }
        });

        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCouponApi(binding.tvCouponCode.getText().toString().trim());
            }
        });

        getAllCoupons();


    }

    private void getAllCoupons() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CouponResponse> call = apiService.getCoupons(userModel.getmUserId());
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            couponList.clear();
                            couponList.addAll(response.body().getCoupons());
                            if (couponList.size() > 0) {
                                binding.noData.setVisibility(View.GONE);
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
                            }
                            couponAdapter.notifyDataSetChanged();
                        } else {
                            binding.noData.setVisibility(View.VISIBLE);
                            showError(response.body().getMessage());
                        }
                    } else {
                        binding.noData.setVisibility(View.VISIBLE);
                        showError(response.message());
                    }
                } catch (Exception e) {
                    binding.noData.setVisibility(View.VISIBLE);
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                binding.noData.setVisibility(View.VISIBLE);
                hideLoader();
                Log.e("Failure", t.toString());
            }
        });
    }


    private void applyCouponApi(String couponCode) {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CouponResponse> call = apiService.applyCouponCode(userModel.getmUserId(), couponCode);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            CouponModel couponModel = response.body().getCoupons().get(0);
                            double itemTotal = 0.0, minDiscountAmt = 0.0, requiredAmount = 0.0;

                            for (int i = 0; i < cartList.size(); i++) {
                                String price = cartList.get(i).getBottlePrice();
                                int quantity = cartList.get(i).getItemQuantity();
                                itemTotal = itemTotal + (Double.parseDouble(price) * quantity);
                            }
                            minDiscountAmt = Double.parseDouble(couponModel.getmCouponMinAmt());
                            if (itemTotal < minDiscountAmt) {
                                requiredAmount = minDiscountAmt - itemTotal;
                                showError(String.format("Add items worth %s more to apply this coupon", new IndianCurrencyFormat().inCuFormatText(String.valueOf(requiredAmount))));
                            } else {
                                onCouponClick(couponModel);
                            }
                        } else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError("Something went wrong");
                    }
                } catch (Exception e) {
                    binding.noData.setVisibility(View.VISIBLE);
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                binding.noData.setVisibility(View.VISIBLE);
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }


    @Override
    public void onCouponClick(CouponModel couponModel) {
        showAlert("Coupon Applied Successfully");
        Intent intent = new Intent(CouponsActivity.this, CheckoutActivity.class);
        intent.putExtra(Constant.BundleExtras.COUPON_DETAILS, new Gson().toJson(couponModel));
        startActivity(intent);
    }
}