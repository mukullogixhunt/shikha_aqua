package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.logixhunt.shikhaaqua.databinding.CityStateAdapterItemBinding;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.model.StateModel;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.ui.activities.SelectAreaActivity;
import com.logixhunt.shikhaaqua.ui.activities.SelectCityActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.List;

public class CityStateSelectAdapter extends RecyclerView.Adapter<CityStateSelectAdapter.MyViewHolder> {

    Context context;
    List<StateModel> stateList;
    List<CityModel> cityList;
    boolean isState;

    public CityStateSelectAdapter(Context context, List<StateModel> stateList, boolean isState) {
        this.context = context;
        this.stateList = stateList;
        this.isState = isState;
    }

    public CityStateSelectAdapter(Context context, List<CityModel> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityStateAdapterItemBinding binding = CityStateAdapterItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!isState) {

            CityModel cityModel = cityList.get(position);
            holder.binding.tvStateCity.setText(cityModel.getmCityName());

            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtils.setString(Constant.PreferenceConstant.CITY_ID, cityModel.getmCityId(), context);
                    PreferenceUtils.setString(Constant.PreferenceConstant.CITY_NAME, cityModel.getmCityName(), context);
                    Intent intent = new Intent(context, SelectAreaActivity.class);
                    intent.putExtra(Constant.BundleExtras.CITY_ID, cityModel.getmCityId());
                    intent.putExtra(Constant.BundleExtras.CITY_NAME, cityModel.getmCityName());
                    context.startActivity(intent);
                }
            });

        } else {
            StateModel stateModel = stateList.get(position);
            holder.binding.tvStateCity.setText(stateModel.getmStateName());
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SelectCityActivity.class);
                    intent.putExtra(Constant.BundleExtras.STATE_ID, stateModel.getmStateId());
                    PreferenceUtils.setString(Constant.PreferenceConstant.STATE_ID, stateModel.getmStateId(), context);
                    PreferenceUtils.setString(Constant.PreferenceConstant.STATE_NAME, stateModel.getmStateName(), context);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isState) {
            return stateList.size();
        } else {
            return cityList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CityStateAdapterItemBinding binding;

        MyViewHolder(CityStateAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
