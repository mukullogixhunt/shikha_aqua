package com.logixhunt.shikhaaqua.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.UserResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityLoginBinding;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {

    }
    private void initialization() {

        binding.etPhone.requestFocus();

        binding.btnLogin.setOnClickListener(view -> {
            if (validate()) {
                callLoginApi();
            }
        });
//        binding.tvTerms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(LoginActivity.this, WebActivity.class);
//                intent3.putExtra(Constant.BundleExtras.WEBVIEW_TITLE,getResources().getString(R.string.terms_condition));
//                intent3.putExtra(Constant.BundleExtras.WEBVIEW_URL,getResources().getString(R.string.terms_url));
//                startActivity(intent3);
//            }
//        });
    }

    private void callLoginApi() {

        showLoader();

        String userMobile = binding.etPhone.getText().toString();
        /*String password = binding.etPassword.getText().toString();*/

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiInterface.userLogin(userMobile);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            PreferenceUtils.setString(
                                    Constant.PreferenceConstant.USER_DATA,
                                    new Gson().toJson(response.body().getUsers()),
                                    LoginActivity.this);

                            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra(Constant.BundleExtras.USER_ID, response.body().getUsers().get(0).getmUserId());
                            startActivity(intent);

                            //by pass otp verification
                            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                            PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGGEDIN, true, LoginActivity.this);*/

                        }else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError(response.message());
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideLoader();
                showError(getResources().getString(R.string.something_went_wrong));
            }
        });

    }

    private boolean validate() {
        boolean valid = true;
        if (binding.etPhone.getText().toString().length() != 10) {
            binding.etPhone.setError("Please Enter Valid Mobile Number");
            valid = false;
        }

        return valid;
    }

}