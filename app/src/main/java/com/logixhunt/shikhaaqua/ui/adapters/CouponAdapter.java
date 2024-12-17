package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.CouponsAdapterBinding;
import com.logixhunt.shikhaaqua.listeners.RequestCouponClickListener;
import com.logixhunt.shikhaaqua.model.database.ProductCart;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.model.CouponModel;
import java.util.ArrayList;
import java.util.List;

public class CouponAdapter  extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {

    private Context context;
    private List<CouponModel> items;

    private List<ProductCart> cartList;
    private DataService dataService;
    private RequestCouponClickListener requestCouponClickListener;

    public CouponAdapter(Context context, List<CouponModel> items, RequestCouponClickListener requestCouponClickListener) {
        this.context = context;
        this.items = items;
        this.requestCouponClickListener = requestCouponClickListener;
        dataService = new DataService(context);
        cartList = new ArrayList<>();
    }


    @NonNull
    @Override
    public CouponAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CouponsAdapterBinding binding = CouponsAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CouponAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.MyViewHolder holder, int position) {
        CouponModel item = items.get(holder.getAdapterPosition());

        holder.binding.tvTitle.setText(item.getmCouponCode());
        holder.binding.tvCouponCode.setText(item.getmCouponCode());


        cartList.clear();
        cartList = dataService.getAllCartItem();
        double itemTotal = 0.0, couponDiscount = 0.0, minDiscountAmt = 0.0, requiredAmount = 0.0;
        minDiscountAmt = Double.parseDouble(item.getmCouponMinAmt());
        couponDiscount = Double.parseDouble(item.getmCouponDiscount());
        for (int i = 0; i < cartList.size(); i++) {
            String price = cartList.get(i).getBottlePrice();
            int quantity = cartList.get(i).getItemQuantity();
            itemTotal = itemTotal + (Double.parseDouble(price) * quantity);
        }

//        if (item.getCouponDiscountType().equals("2")) {
            couponDiscount = (itemTotal / 100.0f) * couponDiscount;
//        }

        if (itemTotal < minDiscountAmt) {
            holder.binding.badge.setColorFilter(context.getColor(R.color.outline));
            holder.binding.tvApply.setTextColor(context.getColor(R.color.outline));
            holder.binding.tvApply.setBackgroundColor(context.getColor(R.color.outline10));
            holder.binding.tvSavings.setTextColor(context.getColor(R.color.error));
            requiredAmount = minDiscountAmt - itemTotal;
            holder.binding.tvSavings.setText(String.format("Add items worth %s more to unlock", new IndianCurrencyFormat().inCuFormatText(String.valueOf(requiredAmount))));
        } else {
            holder.binding.badge.setColorFilter(context.getColor(R.color.blue));
            holder.binding.tvApply.setTextColor(context.getColor(R.color.onSuccessContainer));
            holder.binding.tvApply.setBackgroundColor(context.getColor(R.color.successContainer));
            holder.binding.tvSavings.setTextColor(context.getColor(R.color.success));
            holder.binding.tvSavings.setText(String.format("Save %s with this code", new IndianCurrencyFormat().inCuFormatText(String.valueOf(couponDiscount))));
        }


        holder.binding.tvDescription.setText(Html.fromHtml(item.getmCouponDescription()));


        holder.binding.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tvViewDetails.setVisibility(View.GONE);
                holder.binding.tvHideDetails.setVisibility(View.VISIBLE);
                holder.binding.detailsLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.binding.tvHideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tvViewDetails.setVisibility(View.VISIBLE);
                holder.binding.tvHideDetails.setVisibility(View.GONE);
                holder.binding.detailsLayout.setVisibility(View.GONE);
            }
        });

        double finalItemTotal = itemTotal;
        double finalMinDiscountAmt = minDiscountAmt;
        holder.binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalItemTotal < finalMinDiscountAmt) {
                    holder.binding.tvSavings.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                } else {
                    requestCouponClickListener.onCouponClick(item);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CouponsAdapterBinding binding;

        MyViewHolder(CouponsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
