package com.logixhunt.shikhaaqua.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import com.logixhunt.shikhaaqua.databinding.SliderLayoutBinding;
import com.logixhunt.shikhaaqua.model.SliderModel;
import com.logixhunt.shikhaaqua.utils.ImagePathDecider;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {

    private Context context;
    private List<SliderModel> items;
    private ViewPager2 viewPager2;

    public SliderAdapter(Context context, List<SliderModel> items, ViewPager2 viewPager2) {
        this.context = context;
        this.items = items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SliderLayoutBinding binding = SliderLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SliderModel item = items.get(position);

        Glide.with(context)
                .load(ImagePathDecider.getBannerImagePath() + item.getmSliderImage())
                .into(holder.binding.sliderImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SliderLayoutBinding binding;

        MyViewHolder(SliderLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
