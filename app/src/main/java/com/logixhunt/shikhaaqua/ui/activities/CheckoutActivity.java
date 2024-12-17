package com.logixhunt.shikhaaqua.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.BuildConfig;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.PhonePeApiClient;
import com.logixhunt.shikhaaqua.api.PhonePeApiInterface;
import com.logixhunt.shikhaaqua.api.response.DeliveryChargeResponse;
import com.logixhunt.shikhaaqua.api.response.DisableDateResponse;
import com.logixhunt.shikhaaqua.api.response.PlaceOrderResponse;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.api.response.phonepe.PhonePeResponse;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.database.service.DataService;
import com.logixhunt.shikhaaqua.databinding.ActivityCheckoutBinding;
import com.logixhunt.shikhaaqua.databinding.BottomSheetDateTimeBinding;
import com.logixhunt.shikhaaqua.databinding.OrderSuccessDialogBinding;
import com.logixhunt.shikhaaqua.listeners.RequestDateSelectionListener;
import com.logixhunt.shikhaaqua.listeners.RequestTimeSelectionListener;
import com.logixhunt.shikhaaqua.model.CouponModel;
import com.logixhunt.shikhaaqua.model.DisableDateModel;
import com.logixhunt.shikhaaqua.model.TimePickerModel;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;
import com.logixhunt.shikhaaqua.ui.adapters.CartAdapter;
import com.logixhunt.shikhaaqua.ui.adapters.DatePickerAdapter;
import com.logixhunt.shikhaaqua.ui.adapters.TimePickerAdapter;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.IndianCurrencyFormat;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;
import com.logixhunt.shikhaaqua.utils.Utils;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends BaseActivity implements RequestTimeSelectionListener, RequestDateSelectionListener {

    private String TAG = CheckoutActivity.class.getSimpleName();
    public static CheckoutActivity checkoutActivity;
    private ActivityCheckoutBinding binding;
    private UserModel userModel = new UserModel();

    private DataService dataService;

    private CartAdapter cartAdapter;

    private List<ProductCart> cartList = new ArrayList<>();

    private CouponModel couponModel;

    private List<DisableDateModel> disabledDateList = new ArrayList<>();

    private String lat = "", lng = "", deliveryAddress = "", deliveryAddressOther = "", deliveryPincode = "", /*payMode = "Cash on Delivery",*/
            payMode = "Online", deliveryDate = "", deliveryTime = "", orderId = "", orderNo = "";

    double itemTotal = 0, deliveryCharge = 0, grandTotal = 0.0, netPayable = 0, couponDiscount = 0.0;

    BottomSheetDateTimeBinding dateTimeBinding;
    Dialog dialog;

    //Phone PE
    String apiEndPoint = "/pg/v1/pay";
    String MERCHANT_USER_ID = "SHIKHAAQUA";
    String SALT_INDEX = "1";
    String merchantTransactionId = String.valueOf(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkoutActivity = this;
        PhonePe.init(CheckoutActivity.this, PhonePeEnvironment.RELEASE, BuildConfig.MERCHANT_ID, null);
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        userModel = getUserData(CheckoutActivity.this);
        if (getIntent().hasExtra(Constant.BundleExtras.COUPON_DETAILS)) {
            String couponJson = getIntent().getStringExtra(Constant.BundleExtras.COUPON_DETAILS);
            couponModel = new Gson().fromJson(couponJson, CouponModel.class);
        }

    }

    private void initialization() {
        dataService = new DataService(this);
        cartList.clear();
        cartList = dataService.getAllCartItem();

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        binding.rvCart.setLayoutManager(linearLayoutManager2);


        binding.tvName.setText(userModel.getmUserName());
        binding.tvMobile.setText(userModel.getmUserMobile());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForExitAndLogout();
            }
        });
        binding.tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForExitAndLogout();
            }
        });
        binding.tvDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForExitAndLogout();
            }
        });

        binding.ivDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, MyAddressActivity.class);
                intent.putExtra(Constant.BundleExtras.IS_FROM, "2");
                startActivity(intent);
            }
        });
        binding.tvDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, MyAddressActivity.class);
                intent.putExtra(Constant.BundleExtras.IS_FROM, "2");
                startActivity(intent);
            }
        });

        binding.couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, CouponsActivity.class);
                startActivity(intent);
            }
        });


        binding.cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.online.setChecked(false);
                payMode = "Cash on Delivery";
            }
        });

        binding.online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cod.setChecked(false);
                payMode = "Online";
            }
        });

        binding.ivDeliveryMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRemarkData();
            }
        });

        binding.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    placeOrder();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (deliveryAddress.isEmpty()) {
            showAlert("Please add Delivery address");
            valid = false;
        } else if (deliveryDate.isEmpty()) {
            showAlert("Please select Delivery date");
            valid = false;
        } else if (deliveryTime.isEmpty()) {
            showAlert("Please select Delivery time");
            valid = false;
        }


        return valid;
    }


    @Override
    protected void onResume() {
        super.onResume();
        onCartUpdate();
//        setAddressData();
        getDeliveryCharge();
        getDisabledDate();
    }

    public void onCartUpdate() {
        cartList.clear();
        cartList = dataService.getAllCartItem();
        if (cartList.size() > 0) {
            cartAdapter = new CartAdapter(this, cartList);
            binding.rvCart.setAdapter(cartAdapter);
        } else {
            onBackPressed();
        }
        setCalculation();
        setAddressData();
    }

    private void setRemarkData() {
        binding.etRemark.setVisibility(View.VISIBLE);
        binding.etRemark.requestFocus();
    }


    private void setAddressData() {
        lat = PreferenceUtils.getString(Constant.PreferenceConstant.LAT, CheckoutActivity.this);
        lng = PreferenceUtils.getString(Constant.PreferenceConstant.LNG, CheckoutActivity.this);


        Log.d("TAG", "preferenceLAT" + PreferenceUtils.getString(Constant.PreferenceConstant.LAT, CheckoutActivity.this));
        Log.d("TAG", "preferenceLNG" + PreferenceUtils.getString(Constant.PreferenceConstant.LNG, CheckoutActivity.this));


        deliveryAddress = PreferenceUtils.getString(Constant.PreferenceConstant.ADDRESS, CheckoutActivity.this);
        deliveryAddressOther = PreferenceUtils.getString(Constant.PreferenceConstant.OTHER_ADDRESS, CheckoutActivity.this);
        deliveryPincode = PreferenceUtils.getString(Constant.PreferenceConstant.PINCODE, CheckoutActivity.this);
        binding.tvDeliveryAddress.setText(deliveryAddress);

        if (!deliveryAddress.isEmpty()) {
            binding.ivDeliveryAddress.setImageResource(R.drawable.ic_edit);
            binding.tvDeliveryAddress.setVisibility(View.VISIBLE);
        } else {
            binding.tvDeliveryAddress.setVisibility(View.GONE);
        }
    }

    private void setCalculation() {
        itemTotal = 0;
        grandTotal = 0.0;
        double tax = 0.0;
        netPayable = 0;
        couponDiscount = 0.0;


        for (int i = 0; i < cartList.size(); i++) {
            String price = cartList.get(i).getBottlePrice();
            int quantity = cartList.get(i).getItemQuantity();
            itemTotal = itemTotal + (Double.parseDouble(price) * quantity);
        }


        if (couponModel != null) {
            binding.selectedCouponLayout.setVisibility(View.VISIBLE);
            couponDiscount = Double.parseDouble(couponModel.getmCouponDiscount());
//            if (couponModel.getCouponDiscountType().equals("2")) {
            couponDiscount = (itemTotal / 100.0f) * couponDiscount;
//            }

            binding.couponCode.setText(String.format("Coupon - (%s)", couponModel.getmCouponCode()));
            binding.tvCoupon.setText(String.format("Code %s applied!", couponModel.getmCouponCode()));
            binding.etCoupon.setText(String.format("You saved %s with this code", new IndianCurrencyFormat().inCuFormatText(String.valueOf(couponDiscount))));
            binding.couponDiscount.setText(String.format("-%s", new IndianCurrencyFormat().inCuFormatText(String.valueOf(couponDiscount))));
        } else {
            binding.selectedCouponLayout.setVisibility(View.GONE);
        }


        grandTotal = itemTotal + tax + deliveryCharge - couponDiscount;
        netPayable = grandTotal;


        binding.tvSubTotal.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(itemTotal)));
        binding.tvDeliveryCharge.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(deliveryCharge)));
        binding.tvGst.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(tax)));
        binding.tvGrandTotal.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(grandTotal)));
        binding.tvPayTotal.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(netPayable)));

    }


    public void openBottomSheetForExitAndLogout() {

        dateTimeBinding = BottomSheetDateTimeBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialog); // Style her
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.setContentView(dateTimeBinding.getRoot());
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) dateTimeBinding.getRoot().getParent());
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(dateTimeBinding.getRoot().getHeight());//get the height dynamically
        });
        bottomSheetDialog.show();

        dateTimeBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        dateTimeBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.yyyyMMdd, Locale.US);

        //Current Date
//        String currentDate = dateFormat.format(new Date());

        //Current Date + 1
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(new Date());
        tempCal.add(Calendar.DAY_OF_MONTH, 1);
        String currentDate = dateFormat.format(tempCal.getTime());


        if (Utils.isCurrentTimeGreaterOrEqual("19:00")) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            currentDate = dateFormat.format(calendar.getTime());
        }


        SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constant.yyyyMMdd, Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(Constant.EEddMMM, Locale.US);

        List<String> datePickerList = Utils.getNext30Days(currentDate);

        List<String> formattedDisabledDateList = new ArrayList<>();
        for (DisableDateModel disabledDate : disabledDateList) {
            Date date = null;
            try {
                date = dateFormat2.parse(disabledDate.getMnDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            formattedDisabledDateList.add(outputDateFormat.format(date));
        }

        // Remove strings from datePickerList that match formattedDisabledDateList
        Iterator<String> iterator = datePickerList.iterator();
        while (iterator.hasNext()) {
            String date = iterator.next();
            if (formattedDisabledDateList.contains(date)) {
                iterator.remove();
            }
        }

        onDateSelected(datePickerList.get(0));
        DatePickerAdapter datePickerAdapter = new DatePickerAdapter(CheckoutActivity.this, datePickerList, this);
        dateTimeBinding.rvCalendar.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this, LinearLayoutManager.HORIZONTAL, false));
        dateTimeBinding.rvCalendar.setAdapter(datePickerAdapter);


        setTime(currentDate);


    }


    private void setTime(String selectedDate) {

        List<TimePickerModel> timePickerList = new ArrayList<>();

        Calendar myCalendar = Calendar.getInstance();
        String currentDate = Utils.getDate(myCalendar.getTimeInMillis(), Constant.yyyyMMdd);

        Log.d("TAG", "setTime: selectedDate " + selectedDate);
        Log.d("TAG", "setTime: currentDate " + currentDate);


        if (!currentDate.equals(selectedDate)) {
//                timePickerList.add(new TimePickerModel("9am", "11am", "10"));
//                timePickerList.add(new TimePickerModel("11am", "1pm", "10"));
//                timePickerList.add(new TimePickerModel("1pm", "3pm", "10"));
            timePickerList.add(new TimePickerModel("2pm", "3pm", "10"));
            timePickerList.add(new TimePickerModel("3pm", "5pm", "10"));
            timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
            timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("9am", "11am");
            onTimeSelected("2pm", "3pm");
        } else {
            if (Utils.isCurrentTimeGreaterOrEqual("17:00")) {
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("7pm", "8pm");
                onTimeSelected("2pm", "3pm");
            } else if (Utils.isCurrentTimeGreaterOrEqual("15:00")) {
                timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("5pm", "7pm");
                onTimeSelected("2pm", "3pm");
            } else if (Utils.isCurrentTimeGreaterOrEqual("13:00")) {
                timePickerList.add(new TimePickerModel("3pm", "5pm", "10"));
                timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("3pm", "5pm");
                onTimeSelected("2pm", "3pm");
            } else if (Utils.isCurrentTimeGreaterOrEqual("11:00")) {
//                timePickerList.add(new TimePickerModel("1pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("2pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("3pm", "5pm", "10"));
                timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("1pm", "3pm");
                onTimeSelected("2pm", "3pm");
            } else if (Utils.isCurrentTimeGreaterOrEqual("09:00")) {
//                timePickerList.add(new TimePickerModel("11am", "1pm", "10"));
//                timePickerList.add(new TimePickerModel("1pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("2pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("3pm", "5pm", "10"));
                timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("11am", "1pm");
                onTimeSelected("2pm", "3pm");
            } else {
//                timePickerList.add(new TimePickerModel("9am", "11am", "10"));
//                timePickerList.add(new TimePickerModel("11am", "1pm", "10"));
//                timePickerList.add(new TimePickerModel("1pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("2pm", "3pm", "10"));
                timePickerList.add(new TimePickerModel("3pm", "5pm", "10"));
                timePickerList.add(new TimePickerModel("5pm", "7pm", "10"));
                timePickerList.add(new TimePickerModel("7pm", "8pm", "10"));
//                onTimeSelected("9am", "11am");
                onTimeSelected("2pm", "3pm");
            }

        }


        TimePickerAdapter timePickerAdapter = new TimePickerAdapter(CheckoutActivity.this, timePickerList, this);
        dateTimeBinding.rvTime.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
        dateTimeBinding.rvTime.setAdapter(timePickerAdapter);
    }

    @Override
    public void onTimeSelected(String fromTime, String toTime) {
        binding.tvDeliveryTime.setText(String.format("%s - %s", fromTime, toTime));
        binding.ivDeliveryTime.setImageResource(R.drawable.ic_edit);
        binding.timeLayout.setVisibility(View.VISIBLE);
        deliveryTime = String.format("%s - %s", fromTime, toTime);
    }

    @Override
    public void onDateSelected(String date) {
        binding.tvDeliveryDate.setText(date);
        binding.ivDeliveryTime.setImageResource(R.drawable.ic_edit);
        binding.timeLayout.setVisibility(View.VISIBLE);
        deliveryDate = Utils.changeDateFormat(Constant.EEddMMM, Constant.MMdd, date);
        deliveryDate = Utils.getCurrentYear() + "-" + deliveryDate;
        setTime(deliveryDate);
    }

    private void placeOrder() {
        showLoader();
        HashMap<String, String> idMap = new HashMap<>();
        HashMap<String, String> qtyMap = new HashMap<>();
        HashMap<String, String> priceMap = new HashMap<>();

        String orderRemark = binding.etRemark.getText().toString();
        String couponId = "";
        if (couponModel != null) {
            couponId = couponModel.getmCouponId();
        }

        for (int i = 0; i < cartList.size(); i++) {
            idMap.put("product[" + i + "][m_bottle_id]", cartList.get(i).getBottleId());
            qtyMap.put("product[" + i + "][m_bottle_qty]", String.valueOf(cartList.get(i).getItemQuantity()));
            priceMap.put("product[" + i + "][m_bottle_price]", cartList.get(i).getBottlePrice());
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PlaceOrderResponse> call = apiService.placeOrder(userModel.getmUserId(), lat, lng, PreferenceUtils.getString(Constant.BundleExtras.AREA_ID, CheckoutActivity.this), deliveryAddress, deliveryAddressOther, deliveryDate, deliveryTime, String.valueOf(deliveryCharge), couponId, String.valueOf(couponDiscount), String.valueOf(netPayable), payMode, orderRemark, idMap, qtyMap, priceMap);
        call.enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            orderId = String.valueOf(response.body().getmOrderId());
                            orderNo = String.valueOf(response.body().getmOrderBillNo());


                            if (binding.online.isChecked()) {
                                openPaymentGateway(netPayable * 100);
                            } else {
                                orderCompleteAlert(true);
                                AsyncTask.execute(() -> {
                                    DatabaseClient.getInstance(CheckoutActivity.this).getAppDatabase().cartMasterDao().deleteAll();
                                });
                            }

                        } else {
                            showError(response.body().getMessage());
                            orderCompleteAlert(false);
                        }
                    } else {
                        showError(response.message());
                        orderCompleteAlert(false);
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    orderCompleteAlert(false);
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                orderCompleteAlert(false);
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });

    }

    private void orderCompleteAlert(boolean isSuccess) {


        dialog = new Dialog(CheckoutActivity.this, R.style.my_dialog);
        OrderSuccessDialogBinding dialogBinding = OrderSuccessDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        if (isSuccess) {
            dialogBinding.ivStatus.setImageResource(R.drawable.order_success);
            dialogBinding.ivStatus.setColorFilter(getColor(R.color.success), android.graphics.PorterDuff.Mode.SRC_IN);
            dialogBinding.successLayout.setVisibility(View.VISIBLE);
            dialogBinding.tvOrderAmount.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(netPayable)));
            dialogBinding.tvOrderNumber.setText(String.format("# %s", orderNo));
            dialogBinding.btnMyOrder.setVisibility(View.VISIBLE);
            dialogBinding.btnTryAgain.setVisibility(View.GONE);
            dialogBinding.tvTitleOrder.setText(R.string.order_placed_successfully);

            dialogBinding.btnMyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    intent.putExtra(Constant.BundleExtras.REDIRECT, "3");
                    startActivity(intent);
                    dialog.dismiss();

                }
            });
        } else {
            dialogBinding.ivStatus.setImageResource(R.drawable.order_failed);
            dialogBinding.ivStatus.setColorFilter(getColor(R.color.error), android.graphics.PorterDuff.Mode.SRC_IN);
            dialogBinding.successLayout.setVisibility(View.GONE);
            dialogBinding.btnMyOrder.setVisibility(View.GONE);
            dialogBinding.btnTryAgain.setVisibility(View.VISIBLE);
            dialogBinding.tvTitleOrder.setText(R.string.your_order_could_not_be_placed_please_try_again);
            dialogBinding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }

        dialog.show();


    }

    private void getDeliveryCharge() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DeliveryChargeResponse> call = apiInterface.getDeliveryCharges(userModel.getmUserId(), PreferenceUtils.getString(Constant.PreferenceConstant.CITY_ID, this));
        call.enqueue(new Callback<DeliveryChargeResponse>() {
            @Override
            public void onResponse(Call<DeliveryChargeResponse> call, Response<DeliveryChargeResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            deliveryCharge = Double.parseDouble(response.body().getDeliveryCharges().get(0).getmChargePrice());
                            setCalculation();
                        } else {

                        }
                    } else {

                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<DeliveryChargeResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }

    private void getDisabledDate() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DisableDateResponse> call = apiInterface.disabledDate();
        call.enqueue(new Callback<DisableDateResponse>() {
            @Override
            public void onResponse(Call<DisableDateResponse> call, Response<DisableDateResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            disabledDateList.addAll(response.body().getDisabledDates());

                        } else {

                        }
                    } else {

                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<DisableDateResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }


    public void phonePePaymentSuccess(String transactionId, String amount, String message) {
        setMessageForSnackbar(message, true);
        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_LONG).show();
        updatePaymentStatus(transactionId);
    }

    private void updatePaymentStatus(String transactionId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.updateOrderPaymentStatus(orderNo, transactionId, "1");
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            orderCompleteAlert(true);
                            AsyncTask.execute(() -> {
                                DatabaseClient.getInstance(CheckoutActivity.this).getAppDatabase().cartMasterDao().deleteAll();
                            });
                        } else {
                            orderCompleteAlert(false);
                        }
                    } else {
                        orderCompleteAlert(false);
                    }
                } catch (Exception e) {
                    orderCompleteAlert(false);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
                orderCompleteAlert(false);
            }
        });
    }

    public void phonePePaymentError(String message) {
        orderCompleteAlert(false);
        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void openPaymentGateway(double amount) {

        try {
            merchantTransactionId = String.valueOf(System.currentTimeMillis());
            JSONObject payload = new JSONObject();
            payload.put("merchantTransactionId", merchantTransactionId);
            payload.put("merchantId", BuildConfig.MERCHANT_ID);
            payload.put("merchantUserId", MERCHANT_USER_ID);
            payload.put("amount", amount);
            payload.put("callbackUrl", "https://api.phonepe.com/apis/hermes");
            payload.put("mobileNumber", "7909951312");

            JSONObject paymentInstrument = new JSONObject();
            paymentInstrument.put("type", "PAY_PAGE");
            payload.put("paymentInstrument", paymentInstrument);

            // Convert payload to string
            String payloadString = payload.toString();
            Log.d("2415 payloadString", payloadString);

            // Encode payload using Base64
            String base64Body = Base64.encodeToString(payloadString.getBytes(), Base64.DEFAULT);
            Log.d("2415 base64Body", base64Body);

            String salt = BuildConfig.SALT_KEY;
            String saltIndex = SALT_INDEX;

            // Construct the checksum
            String checksum = Utils.getSha256Hash(base64Body + apiEndPoint + salt) + "###" + saltIndex;
            Log.d("2415 Checksum", checksum);


            B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                    .setData(base64Body)
                    .setChecksum(checksum)
                    .setUrl(apiEndPoint)
                    .build();

            try {
                PhonePe.setFlowId("2415"); // Recommended, not mandatory , An alphanumeric string without any special character
                List<UPIApplicationInfo> upiApps = PhonePe.getUpiApps();
                Intent phonePeIntent = PhonePe.getImplicitIntent(CheckoutActivity.this, b2BPGRequest, "com.phonepe.app");
                phonePeLauncher.launch(phonePeIntent);

            } catch (PhonePeInitException exception) {
                exception.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private final ActivityResultLauncher<Intent> phonePeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        int resultCode = result.getResultCode();
        Intent data = result.getData();

        // Construct the response checksum
        String checksum = Utils.getSha256Hash("/pg/v1/status/" + BuildConfig.MERCHANT_ID + "/" + merchantTransactionId + BuildConfig.SALT_KEY);
        Log.d("2415 Response Checksum", checksum + "###" + SALT_INDEX);


        if (resultCode == RESULT_OK) {
            // Handle successful result
            checkStatus(checksum);
        } else if (resultCode == RESULT_CANCELED) {
            phonePePaymentError("Payment Cancelled");
        } else {
            phonePePaymentError("Payment Failed");
        }
    });

    private void checkStatus(String checksum) {

        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("X-VERIFY", checksum + "###" + SALT_INDEX);
        header.put("X-MERCHANT-ID", BuildConfig.MERCHANT_ID);

        PhonePeApiInterface apiService = PhonePeApiClient.getPhonePeClient().create(PhonePeApiInterface.class);
        Call<PhonePeResponse> call = apiService.checkStatus(BuildConfig.MERCHANT_ID, merchantTransactionId, header);
        call.enqueue(new Callback<PhonePeResponse>() {
            @Override
            public void onResponse(Call<PhonePeResponse> call, Response<PhonePeResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        assert response.body() != null;
                        Log.d(TAG, "Phone Pe Response: " + response.body());
                        if (response.body().success) {
                            if (response.body().data.state.equals("COMPLETED")) {
                                String transactionId = response.body().data.transactionId;
                                String amount = String.valueOf(response.body().data.amount);
                                phonePePaymentSuccess(transactionId, amount, "Payment Successful");
                            } else {
                                phonePePaymentError("Payment Failed");
                            }
                        } else {
                            phonePePaymentError("Payment was not successful");
                        }
                    } else {
                        Toast.makeText(CheckoutActivity.this, "response error", Toast.LENGTH_SHORT).show();
                        phonePePaymentError("Failed to get payment status");
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "Phone Pe Response Exception: ", e);
                    phonePePaymentError("Failed to get payment status");
                }
            }

            @Override
            public void onFailure(Call<PhonePeResponse> call, Throwable t) {
                Log.e("Phone Pe Response Failure", t.toString());
                phonePePaymentError("Failed to get payment status");
            }
        });

    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {
            Snacky.builder()
                    .setActivity(CheckoutActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();
        } else {
            Snacky.builder()
                    .setActivity(CheckoutActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }


}

