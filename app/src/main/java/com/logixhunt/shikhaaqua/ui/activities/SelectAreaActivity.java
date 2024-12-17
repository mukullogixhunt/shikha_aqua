package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.AreaResponse;
import com.logixhunt.shikhaaqua.api.response.CityResponse;
import com.logixhunt.shikhaaqua.databinding.ActivitySelectAreaBinding;
import com.logixhunt.shikhaaqua.model.AreaModel;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.ui.adapters.AreaSelectAdapter;
import com.logixhunt.shikhaaqua.ui.adapters.CityStateSelectAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAreaActivity extends BaseActivity {

    private ActivitySelectAreaBinding binding;

    private AreaSelectAdapter areaSelectAdapter;

    private List<AreaModel> areaList = new ArrayList<>();
    String city_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAreaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        city_id = getIntent().getStringExtra(Constant.BundleExtras.CITY_ID);
    }

    private void initialization() {

        callAreaApi();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.rvArea.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        areaSelectAdapter = new AreaSelectAdapter(this, areaList);
        binding.rvArea.setAdapter(areaSelectAdapter);


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    getSearch(editable.toString());
                }
            }
        });

    }

    private void getSearch(String text) {
        List<AreaModel> filteredList = new ArrayList<>();
        for (AreaModel item : areaList) {
            if (item.getmAreaName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        areaSelectAdapter = new AreaSelectAdapter(SelectAreaActivity.this, filteredList);
        binding.rvArea.setAdapter(areaSelectAdapter);

    }

    private void callAreaApi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AreaResponse> call = apiInterface.getArea(city_id);
        call.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            areaList.addAll(response.body().getAreas());
                            areaSelectAdapter.notifyDataSetChanged();

                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                hideLoader();
                showError(getString(R.string.something_went_wrong));
            }
        });
    }

}