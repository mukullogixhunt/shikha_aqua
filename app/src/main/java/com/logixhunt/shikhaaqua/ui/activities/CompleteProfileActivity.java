package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.UserResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityCompleteProfileBinding;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;
import com.logixhunt.shikhaaqua.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileActivity extends BaseActivity {

    private ActivityCompleteProfileBinding binding;

    private UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        String userJson = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, CompleteProfileActivity.this);
        userModel = new Gson().fromJson(userJson, UserModel.class);
    }

    private void initialization() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.etMobile.setText(String.format("+91-%s", userModel.getmUserMobile()));

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callUpdateProfileApi();
                }

            }
        });
    }


    private void callUpdateProfileApi() {
        showLoader();
        String userId = userModel.getmUserId();
        String userName = binding.etName.getText().toString().trim();
        String userEmail = binding.etEmail.getText().toString().trim();


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.updateUserProfile(userId, userName, userEmail, "", "", "", "");
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            if (response.body().getUsers().size() > 0) {
                                showAlert(response.body().getMessage());

                                PreferenceUtils.setString(
                                        Constant.PreferenceConstant.USER_DATA,
                                        new Gson().toJson(response.body().getUsers().get(0)),
                                        CompleteProfileActivity.this);
                                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGGED_IN, true, CompleteProfileActivity.this);
                                getPreferenceData();


                                Intent intent = new Intent(CompleteProfileActivity.this, SelectStateActivity.class);
                                startActivity(intent);
                                finishAffinity();


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


    private boolean validate() {
        boolean valid = true;
        if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError("Please Enter Your Name");
            valid = false;
        }

        return valid;
    }


}