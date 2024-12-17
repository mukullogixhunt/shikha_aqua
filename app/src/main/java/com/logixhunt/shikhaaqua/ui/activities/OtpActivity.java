package com.logixhunt.shikhaaqua.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.UserResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityOtpBinding;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {
    private String TAG = OtpActivity.class.getSimpleName();

    private ActivityOtpBinding binding;
    private CountDownTimer countDownTimer;
    private int countDown = 120000;
    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        userId = getIntent().getStringExtra((Constant.BundleExtras.USER_ID));
    }

    private void initialization() {

        callSendOtpApi();
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    callVerifyOtpApi();
                }
            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSendOtpApi();
            }
        });

    }

    private boolean isValidate() {
        if (binding.otpView.getOTP().isEmpty()) {
            showError(getResources().getString(R.string.please_enter_otp));
            return false;
        }
        if (binding.otpView.getOTP().length() < 6) {
            showError(getResources().getString(R.string.please_enter_valid_otp));
            return false;
        }
        return true;
    }


    private void callSendOtpApi() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.sendOtp(userId);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            if (response.body().getUsers().size() > 0) {
                                showAlert(response.body().getMessage());
                                userId = response.body().getUsers().get(0).getmUserId();
                            }
                        } else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
        setUpTimerForResend();
    }


    private void callVerifyOtpApi() {
        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.verifyOtp(userId, binding.otpView.getOTP());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            if (response.body().getUsers().size() > 0) {

                                PreferenceUtils.setString(
                                        Constant.PreferenceConstant.USER_DATA,
                                        new Gson().toJson(response.body().getUsers().get(0)),
                                        OtpActivity.this);

                                UserModel user = response.body().getUsers().get(0);


                                if (user.getmUserName() != null && !user.getmUserName().isEmpty()) {
                                    Intent intent;
                                    if (PreferenceUtils.getString(Constant.PreferenceConstant.STATE_ID, OtpActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.STATE_ID, OtpActivity.this).isEmpty() && PreferenceUtils.getString(Constant.PreferenceConstant.CITY_ID, OtpActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.CITY_ID, OtpActivity.this).isEmpty() && PreferenceUtils.getString(Constant.PreferenceConstant.AREA_ID, OtpActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.AREA_ID, OtpActivity.this).isEmpty()) {
                                        intent = new Intent(OtpActivity.this, MainActivity.class);
                                        PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGGED_IN, true, OtpActivity.this);
                                    } else {
                                        intent = new Intent(OtpActivity.this, SelectStateActivity.class);
                                    }
                                    startActivity(intent);
                                } else {
                                    Intent profileIntent = new Intent(OtpActivity.this, CompleteProfileActivity.class);
                                    profileIntent.putExtra(Constant.BundleExtras.IS_FROM, Constant.OTP_SCREEN);
                                    startActivity(profileIntent);
                                }

                            }
                        } else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }


    private void setUpTimerForResend() {
        binding.resendOtp.setEnabled(false);

        //set timer min
        countDown = 120000; //(120000 sec)

        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(countDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long Days = countDown / (24 * 60 * 60 * 1000);
                long Hours = countDown / (60 * 60 * 1000) % 24;
                long Minutes = countDown / (60 * 1000) % 60;
                long Seconds = countDown / 1000 % 60;
                countDown = countDown - 1000;

                binding.resendOtp.setText(String.format("%02d", Seconds) + " Second left");
            }

            @Override
            public void onFinish() {
                binding.resendOtp.setText(getString(R.string.resend_otp));
                binding.resendOtp.setEnabled(true);
            }
        }.start();
    }


}