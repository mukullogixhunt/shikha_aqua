package com.logixhunt.shikhaaqua.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.databinding.MyAddrerssAdapterBinding;
import com.logixhunt.shikhaaqua.model.database.AddressModel;
import com.logixhunt.shikhaaqua.ui.activities.LocationActivity;
import com.logixhunt.shikhaaqua.ui.activities.MyAddressActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.List;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.MyViewHolder> {


    private List<AddressModel> items;
    private Context context;
    private boolean isBottomSheet;

    public MyAddressAdapter(List<AddressModel> items, Context context, boolean isBottomSheet) {
        this.items = items;
        this.context = context;
        this.isBottomSheet = isBottomSheet;
    }

    @NonNull
    @Override
    public MyAddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyAddrerssAdapterBinding binding = MyAddrerssAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyAddressAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAddressAdapter.MyViewHolder holder, int position) {
        AddressModel item = items.get(position);

        holder.binding.tvAddressTitle.setText(item.getAddressTitle());
        holder.binding.tvAddress.setText(item.getAddress());


        holder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().addressDao().delete(item);
                });
                if (MyAddressActivity.myAddressActivity!=null){
                    MyAddressActivity.myAddressActivity.onAddressUpdate();
                }


            }
        });

        if (isBottomSheet) {
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtils.setString(Constant.PreferenceConstant.LAT,item.getLat(), context);

                    PreferenceUtils.setString(Constant.PreferenceConstant.LNG,item.getLng(),context);

                    PreferenceUtils.setString(Constant.PreferenceConstant.ADDRESS,item.getAddress(),context);
                    PreferenceUtils.setString(Constant.PreferenceConstant.OTHER_ADDRESS,item.getAddress(),context);
                    PreferenceUtils.setString(Constant.PreferenceConstant.PINCODE,item.getPinCode(),context);
                    if (MyAddressActivity.myAddressActivity!=null){
                        MyAddressActivity.myAddressActivity.onBackPressed();
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyAddrerssAdapterBinding binding;

        MyViewHolder(MyAddrerssAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
