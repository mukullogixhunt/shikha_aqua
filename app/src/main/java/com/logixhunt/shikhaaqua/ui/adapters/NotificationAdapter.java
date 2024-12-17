package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.logixhunt.shikhaaqua.databinding.NotificationAdapterBinding;
import com.logixhunt.shikhaaqua.model.NotificationModel;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.Utils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<NotificationModel> items;

    public NotificationAdapter(Context context, List<NotificationModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationAdapterBinding binding = NotificationAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NotificationModel item = items.get(position);

        holder.binding.tvTitle.setText(item.getNotificationText());
        holder.binding.tvDetails.setText(item.getNotificationDesc());
        holder.binding.tvDateTime.setText(Utils.changeDateFormat(Constant.yyyyMMddHHmmss, Constant.ddMMMyyyy, item.getNofificationDate()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NotificationAdapterBinding binding;

        MyViewHolder(NotificationAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
