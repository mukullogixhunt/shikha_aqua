package com.logixhunt.shikhaaqua.ui.fragments.bottomnavigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
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
import com.logixhunt.shikhaaqua.api.response.ProductResponse;
import com.logixhunt.shikhaaqua.api.response.SliderResponse;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.FragmentHomeBinding;
import com.logixhunt.shikhaaqua.databinding.ProductAdapterBinding;
import com.logixhunt.shikhaaqua.model.OrderModel;
import com.logixhunt.shikhaaqua.model.ProductModel;
import com.logixhunt.shikhaaqua.model.SliderModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;
import com.logixhunt.shikhaaqua.ui.activities.CheckoutActivity;
import com.logixhunt.shikhaaqua.ui.activities.LoginActivity;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.ui.activities.NotificationsActivity;
import com.logixhunt.shikhaaqua.ui.activities.SelectStateActivity;
import com.logixhunt.shikhaaqua.ui.adapters.OrderAdapter;
import com.logixhunt.shikhaaqua.ui.adapters.ProductAdapter;
import com.logixhunt.shikhaaqua.ui.adapters.SliderAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseFragment;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    public static HomeFragment homeFragment;
    private UserModel userModel = new UserModel();
    private SliderAdapter sliderAdapter;
    private ProductAdapter productAdapter;
    private DataService dataService;
    private List<SliderModel> sliderList = new ArrayList<>();
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductCart> cartList = new ArrayList<>();
    private Handler sliderHandler = new Handler();
    private int pos = 0;

    private List<OrderModel> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        homeFragment = this;
        getPreferenceData();
        initialization();
        return binding.getRoot();
    }

    private void getPreferenceData() {
        userModel = getUserData(requireActivity());
    }

    private void initialization() {
        dataService = new DataService(requireActivity());
        onCartUpdate();
//        binding.tvUserName.setText(String.format("Hey, %s", userModel.getmUserName()));

//        binding.tvLocation.setText(userModel.getmUserAddress());

        binding.tvLocation.setText(String.format("%s, %s, %s", PreferenceUtils.getString(Constant.PreferenceConstant.AREA_NAME, requireContext()), PreferenceUtils.getString(Constant.PreferenceConstant.CITY_NAME, requireContext()), PreferenceUtils.getString(Constant.PreferenceConstant.STATE_NAME, requireContext())));


        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SelectStateActivity.class);
                startActivity(intent);
            }
        });

        binding.ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+91 9399629422"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        binding.contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+91 9399629422"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = requireActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        sliderAdapter = new SliderAdapter(getActivity(), sliderList, binding.sliderPager);
        binding.sliderPager.setAdapter(sliderAdapter);
        getBanners();

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireContext());
        binding.rvProducts.setLayoutManager(linearLayoutManager2);
        productAdapter = new ProductAdapter(requireActivity(), productList);
        binding.rvProducts.setAdapter(productAdapter);
        getProducts();

        binding.rvRecentOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter(requireContext(), orderList);
        binding.rvRecentOrders.setAdapter(orderAdapter);
        getRecentOrders();

        binding.sliderPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(15));

        binding.sliderPager.setPageTransformer(compositePageTransformer);

        binding.sliderPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3500); // slide duration 2 seconds
            }
        });


    }

    private void getRecentOrders() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiService.getRecentOrder(userModel.getmUserId());
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
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
//                                binding.layoutNoData.text.setText("You haven't placed any order yet");
                            }

                            orderAdapter.notifyDataSetChanged();
                        } else {
                            showError(response.body().getMessage());

                            binding.noData.setVisibility(View.VISIBLE);
//                            binding.layoutNoData.text.setText("You haven't placed any order yet");
                        }
                    } else {
                        showError(response.message());

                        binding.noData.setVisibility(View.VISIBLE);
//                        binding.layoutNoData.text.setText("You haven't placed any order yet");
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);

                    binding.noData.setVisibility(View.VISIBLE);
//                    binding.layoutNoData.text.setText("You haven't placed any order yet");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
//                binding.orderShimmer.stopShimmer();
//                binding.orderShimmer.setVisibility(View.GONE);
                Log.e("Failure", t.toString());
                showError("Something went wrong");
                binding.noData.setVisibility(View.VISIBLE);
//                binding.layoutNoData.text.setText("You haven't placed any order yet");
            }
        });
    }

    public void onCartUpdate() {
        cartList.clear();
        cartList = dataService.getAllCartItem();
        setupCartLayout();
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.sliderPager.getCurrentItem() == (sliderList.size() - 1)) {
                pos = 0;
            } else {
                pos = binding.sliderPager.getCurrentItem() + 1;
            }
            binding.sliderPager.setCurrentItem(pos);
        }
    };

    private void getBanners() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SliderResponse> call = apiService.getSlider();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            sliderList.clear();
                            sliderList.addAll(response.body().getSliders());
                            binding.sliderPager.setVisibility(View.VISIBLE);
                            //  new pager
                            sliderAdapter.notifyDataSetChanged();
                        } else {
                            binding.sliderPager.setVisibility(View.GONE);
                        }
                    } else {
                        binding.sliderPager.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.sliderPager.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void getProducts() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResponse> call = apiService.getBottles();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            productList.clear();
                            productList.addAll(response.body().getProducts());
                            syncWithLocalData();
                            productAdapter.notifyDataSetChanged();

//                            binding.noData.setVisibility(View.GONE);
                            binding.rvProducts.setVisibility(View.VISIBLE);
                        } else {
//                            binding.noData.setVisibility(View.VISIBLE);
                            binding.rvProducts.setVisibility(View.GONE);
                        }
                    } else {
//                        binding.noData.setVisibility(View.VISIBLE);
                        binding.rvProducts.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
//                    binding.noData.setVisibility(View.VISIBLE);
                    binding.rvProducts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
//                binding.noData.setVisibility(View.VISIBLE);
                binding.rvProducts.setVisibility(View.GONE);
            }
        });
    }

    private void syncWithLocalData() {
        for (int i = 0; i < productList.size(); i++) {
            if (cartList.size() > 0) {
                for (int j = 0; j < cartList.size(); j++) {
                    if (productList.get(i).getmBottleId().equals(cartList.get(j).getBottleId())) {
                        productList.get(i).setItemQuantity(cartList.get(j).getItemQuantity());
                    }
                }
            } else {
                productList.get(i).setItemQuantity(0);
            }
        }
    }


    public void setupCartLayout() {

        if (cartList.size() > 0) {
            binding.cartLayout.setVisibility(View.VISIBLE);
        } else {
            binding.cartLayout.setVisibility(View.GONE);
        }


        double totalAmount = 0.0;
        for (int i = 0; i < cartList.size(); i++) {
            String price = cartList.get(i).getBottlePrice();
            int quantity = cartList.get(i).getItemQuantity();
            totalAmount = totalAmount + Double.parseDouble(price) * quantity;
        }

        if (dataService.getAllCartItem().size() > 0) {
            binding.cartLayout.setVisibility(View.VISIBLE);
        } else {
            binding.cartLayout.setVisibility(View.GONE);
        }

        binding.cartTotal.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(totalAmount)));
        binding.cartCount.setText(String.valueOf(cartList.size()));

        binding.cartLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        checkUserApi();
        sliderHandler.removeCallbacks(sliderRunnable);
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

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3500);
        sliderAdapter.notifyDataSetChanged();
        onCartUpdate();
    }
}