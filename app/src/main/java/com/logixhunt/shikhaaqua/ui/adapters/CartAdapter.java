package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.ProductAdapterBinding;
import com.logixhunt.shikhaaqua.model.ProductModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;
import com.logixhunt.shikhaaqua.ui.activities.CheckoutActivity;
import com.logixhunt.shikhaaqua.ui.fragments.bottomnavigation.HomeFragment;
import com.logixhunt.shikhaaqua.utils.ImagePathDecider;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {


    private Context context;
    private List<ProductCart> items;


    public CartAdapter(Context context, List<ProductCart> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductAdapterBinding binding = ProductAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        ProductCart item = items.get(holder.getAdapterPosition());

        holder.binding.tvProduct.setText(item.getBottleCompany());
        holder.binding.tvSize.setText(String.format("%s", item.getBottleSize()));
        holder.binding.tvPrice.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(item.getBottlePrice())));

        Glide.with(context)
                .load(ImagePathDecider.getProductImagePath() + item.getBottleImage())
                .error(R.drawable.water_jar)
                .into(holder.binding.ivProduct);

        if (item.getItemQuantity() > 0) {
            holder.binding.btnOrder.setVisibility(View.GONE);
            holder.binding.qtyLayout.setVisibility(View.VISIBLE);
            holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));
        } else {
            holder.binding.btnOrder.setVisibility(View.VISIBLE);
            holder.binding.qtyLayout.setVisibility(View.GONE);
        }

        holder.binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setItemQuantity(item.getItemQuantity() + 1);


                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().insert(item);
                });

                holder.binding.btnOrder.setVisibility(View.GONE);
                holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }

                if (CheckoutActivity.checkoutActivity != null) {
                    CheckoutActivity.checkoutActivity.onCartUpdate();
                }

            }
        });

        holder.binding.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setItemQuantity(item.getItemQuantity() + 1);

                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().updateCartItem(item);
                });
                holder.binding.btnOrder.setVisibility(View.GONE);
                holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }

                if (CheckoutActivity.checkoutActivity != null) {
                    CheckoutActivity.checkoutActivity.onCartUpdate();
                }
            }
        });

        holder.binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getItemQuantity() > 1) {
                    item.setItemQuantity(item.getItemQuantity() - 1);


                    AsyncTask.execute(() -> {
                        DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().updateCartItem(item);
                    });

                    holder.binding.btnOrder.setVisibility(View.GONE);
                    holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                    holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));
                } else {
                    item.setItemQuantity(0);

                    AsyncTask.execute(() -> {
                        DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().delete(item);
                    });

                    holder.binding.btnOrder.setVisibility(View.VISIBLE);
                    holder.binding.qtyLayout.setVisibility(View.GONE);
                }

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }
                if (CheckoutActivity.checkoutActivity != null) {
                    CheckoutActivity.checkoutActivity.onCartUpdate();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProductAdapterBinding binding;

        MyViewHolder(ProductAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
