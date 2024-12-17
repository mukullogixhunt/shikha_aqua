package com.logixhunt.shikhaaqua.ui.fragments.bottomnavigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.OrderResponse;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.databinding.FragmentOrdersBinding;
import com.logixhunt.shikhaaqua.model.OrderModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.activities.LoginActivity;
import com.logixhunt.shikhaaqua.ui.adapters.OrderAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseFragment;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends BaseFragment {

    private FragmentOrdersBinding binding;

    private UserModel userModel = new UserModel();

    private List<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(getLayoutInflater(), container, false);
        getPreferenceData();
        initialization();
        return binding.getRoot();
    }

    private void getPreferenceData() {
        userModel = getUserData(requireActivity());
    }

    private void initialization() {
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter(requireContext(), orderList);
        binding.rvOrders.setAdapter(orderAdapter);
        getOrders();
    }

    private void getOrders() {
//        binding.orderShimmer.setVisibility(View.VISIBLE);
//        binding.orderShimmer.startShimmer();
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiService.getOrder(userModel.getmUserId(), "");
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
//                binding.orderShimmer.stopShimmer();
//                binding.orderShimmer.setVisibility(View.GONE);
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            orderList.clear();
                            orderList.addAll(response.body().getOrders());

                            if (orderList.size() > 0) {
                                binding.noData.setVisibility(View.GONE);
//                                binding.layoutNoData.layoutNoData.setVisibility(View.GONE);
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
//                                binding.layoutNoData.layoutNoData.setVisibility(View.VISIBLE);
//                                binding.layoutNoData.text.setText("You haven't placed any order yet");
                            }

                            orderAdapter.notifyDataSetChanged();
                        } else {
                            showError(response.body().getMessage());
                            binding.noData.setVisibility(View.VISIBLE);
//                            binding.layoutNoData.layoutNoData.setVisibility(View.VISIBLE);
//                            binding.layoutNoData.text.setText("You haven't placed any order yet");
                        }
                    } else {
                        showError(response.message());
                        binding.noData.setVisibility(View.VISIBLE);
//                        binding.layoutNoData.layoutNoData.setVisibility(View.VISIBLE);
//                        binding.layoutNoData.text.setText("You haven't placed any order yet");
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.noData.setVisibility(View.VISIBLE);
//                    binding.layoutNoData.layoutNoData.setVisibility(View.VISIBLE);
//                    binding.layoutNoData.text.setText("You haven't placed any order yet");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
//                binding.orderShimmer.stopShimmer();
//                binding.orderShimmer.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                Log.e("Failure", t.toString());
                showError("Something went wrong");
//                binding.layoutNoData.layoutNoData.setVisibility(View.VISIBLE);
//                binding.layoutNoData.text.setText("You haven't placed any order yet");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserApi();
    }

    private void checkUserApi() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.checkUsers(userModel.getmUserMobile());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            Log.d("TAG", "Check User:  Successful");
                        } else {
                            PreferenceUtils.clearAll(requireActivity());
                            FirebaseApp.initializeApp(requireActivity());
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    log_e("TAG", "", task.getException());
                                    return;
                                }
                                String notificationToken = task.getResult();
                                log_d("TAG", "token=====>" + notificationToken);
                                PreferenceUtils.setString(
                                        Constant.PreferenceConstant.FIREBASE_TOKEN,
                                        notificationToken,
                                        requireActivity()
                                );
                            });
                            AsyncTask.execute(() -> {
                                DatabaseClient.getInstance(requireActivity()).getAppDatabase().addressDao().deleteAll();
                            });

                            Toast.makeText(requireActivity(), "You have been blocked from using this App", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireActivity(), LoginActivity.class);
                            startActivity(intent);
                            requireActivity().finishAffinity();
                        }
                    } else {
                        Log.d("TAG", "FCM UPDATE:  Failed");
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }


}