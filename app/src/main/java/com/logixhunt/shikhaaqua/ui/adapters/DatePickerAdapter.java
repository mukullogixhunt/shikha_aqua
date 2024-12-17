package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.databinding.DatePickerAdapterBinding;
import com.logixhunt.shikhaaqua.databinding.TimePickerAdapterBinding;
import com.logixhunt.shikhaaqua.listeners.RequestDateSelectionListener;
import com.logixhunt.shikhaaqua.listeners.RequestTimeSelectionListener;
import com.logixhunt.shikhaaqua.model.TimePickerModel;

import java.util.List;

public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.MyViewHolder> {

    private Context context;
    private List<String> items;
    private RequestDateSelectionListener requestDateSelectionListener;
    private int lastChecked = 0;

    public DatePickerAdapter(Context context, List<String> items, RequestDateSelectionListener requestDateSelectionListener) {
        this.context = context;
        this.items = items;
        this.requestDateSelectionListener = requestDateSelectionListener;
    }

    @NonNull
    @Override
    public DatePickerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DatePickerAdapterBinding binding = DatePickerAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DatePickerAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DatePickerAdapter.MyViewHolder holder, int position) {
        String item = items.get(holder.getAdapterPosition());

        String[] splitDate =item.split(",");

        holder.binding.tvDay.setText(splitDate[0]);
        holder.binding.tvDate.setText(splitDate[1]);

        if (lastChecked == holder.getAdapterPosition()){
            holder.binding.dateCard.setCardBackgroundColor(context.getColor(R.color.primary));
            holder.binding.tvDay.setTextColor(context.getColor(R.color.onPrimary));
            holder.binding.tvDate.setTextColor(context.getColor(R.color.surface));
        }else {
            holder.binding.dateCard.setCardBackgroundColor(context.getColor(R.color.cardBackground));
            holder.binding.tvDay.setTextColor(context.getColor(R.color.onSurface));
            holder.binding.tvDate.setTextColor(context.getColor(R.color.onSurfaceVariant));
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastChecked = holder.getAdapterPosition();
                requestDateSelectionListener.onDateSelected(item);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DatePickerAdapterBinding binding;
        MyViewHolder(DatePickerAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
