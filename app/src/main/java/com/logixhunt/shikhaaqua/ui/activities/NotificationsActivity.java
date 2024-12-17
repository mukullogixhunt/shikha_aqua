package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.NotificationResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityNotificationsBinding;
import com.logixhunt.shikhaaqua.model.NotificationModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.adapters.NotificationAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseActivity {

    private ActivityNotificationsBinding binding;

    private UserModel userModel = new UserModel();

    private NotificationAdapter notificationAdapter;

    private List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }
    private void getPreferenceData() {
        userModel = getUserData(NotificationsActivity.this);
    }

    private void initialization() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.rvNotification.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notificationList);
        binding.rvNotification.setAdapter(notificationAdapter);
        getNotifications();
    }

    private void getNotifications() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NotificationResponse> call = apiInterface.getNotifications(userModel.getmUserId());

        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            notificationList.clear();
                            notificationList.addAll(response.body().getNotifications());
                            notificationAdapter.notifyDataSetChanged();
                            if (notificationList.size() > 0) {
                                binding.noData.setVisibility(View.GONE);
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.noData.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                hideLoader();
                showError(getResources().getString(R.string.something_went_wrong));
                binding.noData.setVisibility(View.VISIBLE);
            }
        });
    }
}