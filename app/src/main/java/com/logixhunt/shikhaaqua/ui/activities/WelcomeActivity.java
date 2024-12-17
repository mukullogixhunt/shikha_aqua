package com.logixhunt.shikhaaqua.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.databinding.ActivityWelcomeBinding;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;

    private ViewPagerAdapter viewPagerAdapter;

    private int images[] = {R.drawable.intro1, R.drawable.intro2, R.drawable.intro3};

    private int headings[] = {R.string.heading1, R.string.heading2, R.string.heading3};

    private int descriptions[] = {R.string.desc1, R.string.desc2, R.string.desc3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
    }

    private void initialization() {
        binding.tvTitle.setText(headings[0]);
        binding.tvDesc.setText(descriptions[0]);

        viewPagerAdapter = new ViewPagerAdapter();
        binding.viewPager.setAdapter(viewPagerAdapter);

        binding.dotsIndicator.setViewPager(binding.viewPager);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    binding.btnNext.setVisibility(View.GONE);
                    binding.tvSkip.setVisibility(View.GONE);
                    binding.btnStart.setVisibility(View.VISIBLE);
                } else {
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.tvSkip.setVisibility(View.VISIBLE);
                    binding.btnStart.setVisibility(View.GONE);
                }

                binding.tvTitle.setText(headings[position]);
                binding.tvDesc.setText(descriptions[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < images.length) {
                    // move to next screen
                    binding.viewPager.setCurrentItem(current);
                }
            }
        });

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.FIRST_LAUNCH_COMPLETE, true, WelcomeActivity.this);
                startActivity(intent);
                finishAffinity();


            }
        });

        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.FIRST_LAUNCH_COMPLETE, true, WelcomeActivity.this);
                startActivity(intent);
                finishAffinity();


            }
        });
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    /**
     * View pager adapter
     */

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.welcome_slider_layout, container, false);
            container.addView(view);

            ImageView sliderImage = (ImageView) view.findViewById(R.id.sliderImage);

            sliderImage.setImageResource(images[position]);


            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}