package com.logixhunt.shikhaaqua.ui.fragments.bottomnavigation;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.logixhunt.shikhaaqua.BuildConfig;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.PhonePeApiClient;
import com.logixhunt.shikhaaqua.api.PhonePeApiInterface;
import com.logixhunt.shikhaaqua.api.response.BalanceResponse;
import com.logixhunt.shikhaaqua.api.response.OrderResponse;
import com.logixhunt.shikhaaqua.api.response.PaymentResponse;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.api.response.phonepe.PhonePeResponse;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.databinding.BottomSheetAmountBinding;
import com.logixhunt.shikhaaqua.databinding.CollectionSuccessDialogBinding;
import com.logixhunt.shikhaaqua.databinding.FragmentAccountBinding;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.activities.EditProfileActivity;
import com.logixhunt.shikhaaqua.ui.activities.LoginActivity;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.ui.activities.MyAddressActivity;
import com.logixhunt.shikhaaqua.ui.activities.NotificationsActivity;
import com.logixhunt.shikhaaqua.ui.activities.OrderDetailsActivity;
import com.logixhunt.shikhaaqua.ui.activities.WebViewActivity;
import com.logixhunt.shikhaaqua.ui.common.BaseFragment;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends BaseFragment {

    private FragmentAccountBinding binding;

    private UserModel userModel = new UserModel();
    double balanceAmount = 0;
    private String TAG = AccountFragment.class.getSimpleName();
    //Phone PE
    String apiEndPoint = "/pg/v1/pay";
    String MERCHANT_USER_ID = "SHIKHAAQUA";
    String SALT_INDEX = "1";
    String merchantTransactionId = String.valueOf(System.currentTimeMillis());
    String transId = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        binding = FragmentAccountBinding.inflate(getLayoutInflater(), container, false);
        PhonePe.init(requireActivity(), PhonePeEnvironment.RELEASE, BuildConfig.MERCHANT_ID, null);
        getPreferenceData();
        initialization();
        return binding.getRoot();
    }

    private void getPreferenceData() {
        userModel = getUserData(requireContext());
    }

    private void initialization() {
        binding.tvName.setText(String.format(userModel.getmUserName()));
        binding.tvInitial.setText(userModel.getmUserName().substring(0, 1));
        binding.tvMobile.setText(String.format("+91-%s", String.format(userModel.getmUserMobile())));

        binding.btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForPayment();
            }
        });

        binding.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MyAddressActivity.class);
                startActivity(intent);
            }
        });

        binding.ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.putExtra(Constant.BundleExtras.REDIRECT, "3");
                startActivity(intent);
            }
        });

        binding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        binding.aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.about_us);
                Constant.WEBVIEW_URL = getString(R.string.about_us_url);
                Intent intent = new Intent(requireActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        binding.termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.terms_and_conditions);
                Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
                Intent intent = new Intent(requireActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        binding.privacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.WEBVIEW_TITLE = getString(R.string.privacy_and_policy);
                Constant.WEBVIEW_URL = getString(R.string.privacy_policy_url);
                Intent intent = new Intent(requireActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });


        binding.logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.openBottomSheetForExitAndLogout(true);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        checkUserApi();
        getPaymentBalance();

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

    private void getPaymentBalance() {
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BalanceResponse> call = apiService.getPaymentBalance(userModel.getmUserId());
        call.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            balanceAmount = response.body().getBalance();
                            if (balanceAmount>0){
                                binding.btnPayNow.setEnabled(true);
                            }
                        }
                    }
                    binding.tvBalance.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(balanceAmount)));
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.tvBalance.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(balanceAmount)));
                }
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
                hideLoader();
                binding.tvBalance.setText(new IndianCurrencyFormat().inCuFormatText(String.valueOf(balanceAmount)));
            }
        });
    }

    private void insertPayment(String amount) {
        showLoader();
        merchantTransactionId = String.valueOf(System.currentTimeMillis());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentResponse> call = apiService.insertPayment(userModel.getmUserId(), amount, merchantTransactionId);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            double payAmount = Double.parseDouble(amount);
                            transId = String.valueOf(response.body().getmTransId());
                            openPaymentGateway(payAmount * 100);
                        }else {
                            collectionCompleteAlert(false);
                        }
                    }else {
                        collectionCompleteAlert(false);
                    }
                } catch (Exception e) {
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    collectionCompleteAlert(false);
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                hideLoader();
                collectionCompleteAlert(false);
            }
        });
    }


    public void openBottomSheetForPayment() {
        BottomSheetAmountBinding bottomBinding = BottomSheetAmountBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.bottomSheetDialog); // Style her
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.setContentView(bottomBinding.getRoot());

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomBinding.getRoot().getParent());
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(bottomBinding.getRoot().getHeight());//get the height dynamically
        });

        bottomSheetDialog.show();

        bottomBinding.etPaymentAmount.setText(String.valueOf(balanceAmount));

        bottomBinding.ivClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomBinding.btnPay.setOnClickListener(v -> {
            if (!bottomBinding.etPaymentAmount.getText().toString().isEmpty()) {
                double payAmount = Double.parseDouble(bottomBinding.etPaymentAmount.getText().toString());
                if (payAmount < 1) {
                    bottomBinding.etPaymentAmount.setError("Payment amount cannot be 0");
                    bottomBinding.etPaymentAmount.requestFocus();
                } else {
                    insertPayment(bottomBinding.etPaymentAmount.getText().toString());

                    bottomSheetDialog.dismiss();
                }
            } else {
                bottomBinding.etPaymentAmount.setError("Please enter payment amount");
                bottomBinding.etPaymentAmount.requestFocus();
            }

        });

    }

    public void phonePePaymentSuccess(String transactionId, String amount, String message) {
        setMessageForSnackbar(message, true);
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
        updatePaymentStatus(transactionId);
    }

    private void updatePaymentStatus(String transactionId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.updatePaymentStatus(transId, transactionId, "1");
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            collectionCompleteAlert(true);
                        } else {
                            collectionCompleteAlert(false);
                        }
                    } else {
                        collectionCompleteAlert(false);
                    }
                } catch (Exception e) {
                    collectionCompleteAlert(false);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
                collectionCompleteAlert(false);
            }
        });
    }

    public void phonePePaymentError(String message) {
        setMessageForSnackbar(message, false);
        collectionCompleteAlert(false);
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void openPaymentGateway(double amount) {

        try {
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
                Intent phonePeIntent = PhonePe.getImplicitIntent(requireActivity(), b2BPGRequest, "com.phonepe.app");
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
                        Toast.makeText(requireActivity(), "response error", Toast.LENGTH_SHORT).show();
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
                    .setActivity(requireActivity())
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
                    .setActivity(requireActivity())
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

    private void collectionCompleteAlert(boolean isSuccess) {
        Dialog dialog = new Dialog(requireActivity(), R.style.my_dialog2);
        CollectionSuccessDialogBinding dialogBinding = CollectionSuccessDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        if (isSuccess) {
            dialogBinding.ivStatus.setImageResource(R.drawable.order_success);
            dialogBinding.ivStatus.setColorFilter(requireActivity().getColor(R.color.success), android.graphics.PorterDuff.Mode.SRC_IN);
            dialogBinding.btnMyOrder.setVisibility(View.VISIBLE);
            dialogBinding.btnTryAgain.setVisibility(View.GONE);
            dialogBinding.tvTitleOrder.setText(R.string.payment_successful);
            dialogBinding.btnMyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    getPaymentBalance();
                }
            });
        } else {
            dialogBinding.ivStatus.setImageResource(R.drawable.order_failed);
            dialogBinding.ivStatus.setColorFilter(requireActivity().getColor(R.color.error), android.graphics.PorterDuff.Mode.SRC_IN);
            dialogBinding.btnMyOrder.setVisibility(View.GONE);
            dialogBinding.btnTryAgain.setVisibility(View.VISIBLE);
            dialogBinding.tvTitleOrder.setText(R.string.payment_failed);
            dialogBinding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });


        }

        dialog.show();

    }

}