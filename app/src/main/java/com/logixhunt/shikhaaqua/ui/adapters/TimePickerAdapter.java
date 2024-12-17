package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.databinding.CityStateAdapterItemBinding;
import com.logixhunt.shikhaaqua.databinding.TimePickerAdapterBinding;
import com.logixhunt.shikhaaqua.listeners.RequestTimeSelectionListener;
import com.logixhunt.shikhaaqua.model.CityModel;
import com.logixhunt.shikhaaqua.model.StateModel;
import com.logixhunt.shikhaaqua.model.TimePickerModel;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.ui.activities.SelectCityActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.List;

public class TimePickerAdapter extends RecyclerView.Adapter<TimePickerAdapter.MyViewHolder> {

    private Context context;
    private List<TimePickerModel> items;

    private RequestTimeSelectionListener requestTimeSelectionListener;
    private int lastChecked = 0;

    public TimePickerAdapter(Context context, List<TimePickerModel> items, RequestTimeSelectionListener requestTimeSelectionListener) {
        this.context = context;
        this.items = items;
        this.requestTimeSelectionListener = requestTimeSelectionListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimePickerAdapterBinding binding = TimePickerAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimePickerModel item = items.get(holder.getAdapterPosition());

        holder.binding.rbTime.setText(String.format("%s - %s", item.getFromTime(), item.getToTime()));
        holder.binding.tvPrice.setText(new IndianCurrencyFormat().inCuFormatText(item.getPrice()));

        if (lastChecked == holder.getAdapterPosition()){
            holder.binding.rbTime.setChecked(true);
        }else {
            holder.binding.rbTime.setChecked(false);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastChecked = holder.getAdapterPosition();
                requestTimeSelectionListener.onTimeSelected(item.getFromTime(),item.getToTime());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TimePickerAdapterBinding binding;

        MyViewHolder(TimePickerAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
