package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.ActivityMyAddressBinding;
import com.logixhunt.shikhaaqua.model.database.AddressModel;
import com.logixhunt.shikhaaqua.ui.adapters.MyAddressAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends BaseActivity {

    private ActivityMyAddressBinding binding;
    public static MyAddressActivity myAddressActivity;

    private MyAddressAdapter myAddressAdapter;
    private List<AddressModel> addressList = new ArrayList<>();
    private DataService dataService;
    private boolean isBottomSheet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myAddressActivity = this;
        initialization();
    }

    private void initialization() {
        dataService = new DataService(this);
        addressList.clear();
        addressList = dataService.getAllAddress();

        if (getIntent().hasExtra(Constant.BundleExtras.IS_FROM)) {
            binding.textView12.setText("Select Delivery Address");
            isBottomSheet = true;
        }


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        binding.rvAddress.setLayoutManager(linearLayoutManager2);


        binding.btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAddressActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        onAddressUpdate();
    }

    public void onAddressUpdate() {
        addressList.clear();
        addressList = dataService.getAllAddress();
        if (addressList.size() > 0) {
            myAddressAdapter = new MyAddressAdapter(addressList, this,isBottomSheet);
            binding.rvAddress.setAdapter(myAddressAdapter);
            binding.noData.setVisibility(View.GONE);
            binding.rvAddress.setVisibility(View.VISIBLE);
        } else {
            binding.noData.setVisibility(View.VISIBLE);
            binding.rvAddress.setVisibility(View.GONE);
        }
    }
}