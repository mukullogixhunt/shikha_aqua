package com.logixhunt.shikhaaqua.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.CityResponse;
import com.logixhunt.shikhaaqua.databinding.ActivitySelectCityBinding;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.ui.adapters.CityStateSelectAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCityActivity extends BaseActivity {

    private ActivitySelectCityBinding binding;

    private CityStateSelectAdapter cityAdapter;

    private List<CityModel> cityList = new ArrayList<>();
    String state_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        state_id = getIntent().getStringExtra(Constant.BundleExtras.STATE_ID);
    }

    private void initialization() {

        callCityApi();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.rvCity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cityAdapter = new CityStateSelectAdapter(this, cityList);
        binding.rvCity.setAdapter(cityAdapter);

    }

    private void callCityApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CityResponse> call = apiInterface.getCity(state_id);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            cityList.addAll(response.body().getCities());
                            cityAdapter.notifyDataSetChanged();

                        }
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                hideLoader();
                showError(getString(R.string.something_went_wrong));
            }
        });
    }
}