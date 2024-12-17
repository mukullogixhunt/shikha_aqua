package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logixhunt.shikhaaqua.databinding.OrderItemAdapterBinding;
import com.logixhunt.shikhaaqua.model.OrderItemModel;
import com.logixhunt.shikhaaqua.utils.ImagePathDecider;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {


    private List<OrderItemModel> items;
    private Context context;

    private RecyclerView recyclerView;

    public OrderItemAdapter(List<OrderItemModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemAdapterBinding binding = OrderItemAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderItemModel item = items.get(holder.getAdapterPosition());

        int itemTotal = Integer.parseInt(item.getmBottlePrice()) *Integer.parseInt(item.getmBottleQty());

        holder.binding.tvItemName.setText(item.getmBottleCompany());
        holder.binding.tvPrice.setText(String.format("%s", item.getmBottlePrice()));
        holder.binding.tvTotal.setText(String.valueOf(itemTotal));
        holder.binding.tvQty.setText(String.format("* %s", item.getmBottleQty()));
        Glide.with(context)
                .load(ImagePathDecider.getProductImagePath() + item.getmBottleImage())
                .into(holder.binding.ivService);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        OrderItemAdapterBinding binding;

        MyViewHolder(OrderItemAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
