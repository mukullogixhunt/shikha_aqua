package com.logixhunt.shikhaaqua.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.StateResponse;
import com.logixhunt.shikhaaqua.databinding.ActivitySelectStateBinding;
import com.logixhunt.shikhaaqua.model.StateModel;
import com.logixhunt.shikhaaqua.ui.adapters.CityStateSelectAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectStateActivity extends BaseActivity {

    private ActivitySelectStateBinding binding;

    private CityStateSelectAdapter stateAdapter;
    private List<StateModel> stateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectStateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {

    }

    private void initialization() {
        callStateApi();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.rvState.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stateAdapter = new CityStateSelectAdapter(this, stateList, true);
        binding.rvState.setAdapter(stateAdapter);
    }

    private void callStateApi() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<StateResponse> call = apiService.getState();
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            stateList.addAll(response.body().getStates());
                            stateAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                hideLoader();
                showError(getString(R.string.something_went_wrong));
            }
        });
    }


}