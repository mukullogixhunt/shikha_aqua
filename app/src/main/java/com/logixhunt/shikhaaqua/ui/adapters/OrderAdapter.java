package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.databinding.OrderAdapterBinding;
import com.logixhunt.shikhaaqua.model.OrderModel;
import com.logixhunt.shikhaaqua.ui.activities.OrderDetailsActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.utils.Utils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context context;
    private List<OrderModel> items;

    public OrderAdapter(Context context, List<OrderModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderAdapterBinding binding = OrderAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        OrderModel item = items.get(holder.getAdapterPosition());

        holder.binding.tvOrderNumber.setText(item.getmOrderBillNo());
        holder.binding.tvPrice.setText(new IndianCurrencyFormat().inCuFormatText(item.getmOrderGTotal()));
        holder.binding.tvPayMode.setText(item.getmOrderPaymode());
        holder.binding.tvDeliveryDate.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMM, item.getmOrderDelvDate()));
        holder.binding.tvDeliveryTime.setText(item.getmOrderDelvTime());
        holder.binding.tvOrderDate.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMMyyyy, item.getmOrderDate()));
        holder.binding.tvOrderTime.setText(Utils.changeDateFormat(Constant.HHmmss, Constant.hhmma, item.getmOrderTime()));


        if (items.size() == 1) {
            holder.binding.orderListLayout.setBackgroundResource(R.drawable.order_list_bg_single);
            holder.binding.viewBorder.setVisibility(View.GONE);
        } else if (holder.getAdapterPosition() == 0) {
            holder.binding.orderListLayout.setBackgroundResource(R.drawable.order_list_bg_first);
            holder.binding.viewBorder.setVisibility(View.VISIBLE);
        } else if (holder.getAdapterPosition() + 1 == items.size()) {
            holder.binding.orderListLayout.setBackgroundResource(R.drawable.order_list_bg_last);
            holder.binding.viewBorder.setVisibility(View.GONE);
        } else {
            holder.binding.orderListLayout.setBackgroundResource(R.drawable.order_list_bg_middle);
            holder.binding.viewBorder.setVisibility(View.VISIBLE);
        }


        switch (item.getmOrderStatus()) {
            case "1":
                holder.binding.tvStatus.setText(context.getResources().getString(R.string.pending));
                holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.tertiary));
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.onTertiary));
                break;
            case "2":
                holder.binding.tvStatus.setText(context.getResources().getString(R.string.accepted));
                holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.tertiary));
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.onTertiary));
                break;
            case "3":
                holder.binding.tvStatus.setText(context.getResources().getString(R.string.in_progress));
                holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.tertiary));
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.onTertiary));
                break;
            case "4":
                holder.binding.tvStatus.setText(context.getResources().getString(R.string.delivered));
                holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.tertiary));
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.onTertiary));
                break;
            case "5":
                holder.binding.tvStatus.setText(context.getResources().getString(R.string.rejected));
                holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.error));
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.onError));
                break;
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(Constant.BundleExtras.ORDER_ID, item.getmOrderId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        OrderAdapterBinding binding;

        MyViewHolder(OrderAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
