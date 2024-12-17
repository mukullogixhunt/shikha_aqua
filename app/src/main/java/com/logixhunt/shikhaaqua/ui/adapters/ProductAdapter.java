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
import com.logixhunt.shikhaaqua.ui.fragments.bottomnavigation.HomeFragment;
import com.logixhunt.shikhaaqua.utils.ImagePathDecider;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {


    private Context context;
    private List<ProductModel> items;

    private List<ProductCart> cartList;
    private DataService dataService;


    public ProductAdapter(Context context, List<ProductModel> items) {
        this.items = items;
        this.context = context;
        this.cartList = new ArrayList<>();
        dataService = new DataService(context);
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductAdapterBinding binding = ProductAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ProductAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        ProductModel item = items.get(holder.getAdapterPosition());

        holder.binding.tvProduct.setText(item.getmBottleCompany());
        holder.binding.tvSize.setText(String.format("%s", item.getmBottleSize()));
        holder.binding.tvPrice.setText(String.format("%s/-", new IndianCurrencyFormat().inCuFormatText(item.getmBottlePrice())));

        Glide.with(context)
                .load(ImagePathDecider.getProductImagePath() + item.getmBottleImage())
                .error(R.drawable.water_jar)
                .into(holder.binding.ivProduct);

        if (item.getItemQuantity() > 0) {
            holder.binding.btnOrder.setVisibility(View.GONE);
            holder.binding.qtyLayout.setVisibility(View.VISIBLE);
            holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));
        }else {
            holder.binding.btnOrder.setVisibility(View.VISIBLE);
            holder.binding.qtyLayout.setVisibility(View.GONE);
        }

        holder.binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setItemQuantity(item.getItemQuantity() + 1);
                ProductCart cart = new ProductCart(item.getmBottleId(), item.getmBottleCompany(), item.getmBottlePrice(), item.getmBottleSize(), item.getmBottleImage(), item.getItemQuantity());

                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().insert(cart);
                });

                holder.binding.btnOrder.setVisibility(View.GONE);
                holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }

            }
        });

        holder.binding.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setItemQuantity(item.getItemQuantity() + 1);
                ProductCart cart = new ProductCart(item.getmBottleId(), item.getmBottleCompany(), item.getmBottlePrice(), item.getmBottleSize(), item.getmBottleImage(), item.getItemQuantity());
                AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().updateCartItem(cart);
                });
                holder.binding.btnOrder.setVisibility(View.GONE);
                holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }
            }
        });

        holder.binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getItemQuantity() > 1) {
                    item.setItemQuantity(item.getItemQuantity() - 1);
                    ProductCart cart = new ProductCart(item.getmBottleId(), item.getmBottleCompany(), item.getmBottlePrice(), item.getmBottleSize(), item.getmBottleImage(), item.getItemQuantity());

                    AsyncTask.execute(() -> {
                        DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().updateCartItem(cart);
                    });

                    holder.binding.btnOrder.setVisibility(View.GONE);
                    holder.binding.qtyLayout.setVisibility(View.VISIBLE);
                    holder.binding.tvQty.setText(String.valueOf(item.getItemQuantity()));
                } else {
                    item.setItemQuantity(0);
                    ProductCart cart = new ProductCart(item.getmBottleId(), item.getmBottleCompany(), item.getmBottlePrice(), item.getmBottleSize(), item.getmBottleImage(), item.getItemQuantity());

                    AsyncTask.execute(() -> {
                        DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().delete(cart);
                    });

                    holder.binding.btnOrder.setVisibility(View.VISIBLE);
                    holder.binding.qtyLayout.setVisibility(View.GONE);
                }

                if (HomeFragment.homeFragment != null) {
                    HomeFragment.homeFragment.onCartUpdate();
                }

            }
        });


    }

    private boolean isItemInCart(String id) {
        boolean exists = false;
        cartList.clear();
        cartList = dataService.getAllCartItem();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getBottleId().equals(id)) {
                exists = true;
            }
        }
        return exists;
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
