package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.databinding.CityStateAdapterItemBinding;
import com.logixhunt.shikhaaqua.model.AreaModel;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.model.StateModel;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.ui.activities.OtpActivity;
import com.logixhunt.shikhaaqua.ui.activities.SelectCityActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.List;

public class AreaSelectAdapter extends RecyclerView.Adapter<AreaSelectAdapter.MyViewHolder> {

    Context context;
    List<AreaModel> areaList;


    public AreaSelectAdapter(Context context, List<AreaModel> areaList) {
        this.context = context;
        this.areaList = areaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityStateAdapterItemBinding binding = CityStateAdapterItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AreaModel areaModel = areaList.get(position);
        holder.binding.tvStateCity.setText(areaModel.getmAreaName());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGGED_IN, true, context);
                Intent intent = new Intent(context, MainActivity.class);
                PreferenceUtils.setString(Constant.PreferenceConstant.AREA_ID, areaModel.getmAreaId(), context);
                PreferenceUtils.setString(Constant.PreferenceConstant.AREA_NAME, areaModel.getmAreaName(), context);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CityStateAdapterItemBinding binding;

        MyViewHolder(CityStateAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
