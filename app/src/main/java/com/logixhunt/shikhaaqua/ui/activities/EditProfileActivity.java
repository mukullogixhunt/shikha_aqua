package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.UserResponse;
import com.logixhunt.shikhaaqua.databinding.ActivityEditProfileBinding;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.ImagePathDecider;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;
import com.logixhunt.shikhaaqua.utils.Utils;

import java.io.File;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;

//    private static final int PERMISSION_CAMERA = 221;
//    private static final int PERMISSION_WRITE_EXTERNAL = 222;
//    private static final int PERMISSION_READ_MEDIA_IMAGES = 223;
//
//    private static final int IMAGE_CAMERA_REQUEST = 440;
//    private static final int IMAGE_GALLERY_REQUEST = 1;

    private final Calendar myCalendar = Calendar.getInstance();

    private UserModel userModel = new UserModel();
    //    private Uri imageUri;
    private String dob = "";

    private String isFrom = "";

    //    private File profileImage = null;
//    private String profileImagePath = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getPreferenceData();
        initialization();
    }

    private void getPreferenceData() {
        String userJson = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, EditProfileActivity.this);
        userModel = new Gson().fromJson(userJson, UserModel.class);
        isFrom = getIntent().getStringExtra(Constant.BundleExtras.IS_FROM);
        setData();
    }

    private void initialization() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.etMobile.setText(String.format("+91-%s", userModel.getmUserMobile()));
        binding.tvInitial.setText(userModel.getmUserName().substring(0, 1));

//        binding.ivCameraProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagePickerDialog();
//            }
//        });

        binding.etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(binding.etDob);
            }
        });


        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataUpdated();
            }
        });

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataUpdated();
            }
        });

        binding.etDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataUpdated();
            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDataUpdated();
            }
        });

//        binding.etPincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                isDataUpdated();
//            }
//        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callUpdateProfileApi();
                }

            }
        });

    }

    private void callUpdateProfileApi() {
        showLoader();
        dob = Utils.changeDateFormat(Constant.ddMMMyyyy, Constant.yyyyMMdd, binding.etDob.getText().toString());
        String userId = userModel.getmUserId();
        String userName = binding.etName.getText().toString().trim();
        String userEmail = binding.etEmail.getText().toString().trim();
        String userDob = dob;
        String userAddress = binding.etAddress.getText().toString().trim();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.updateUserProfile(userId, userName, userEmail, userDob, "", "", userAddress);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            if (response.body().getUsers().size() > 0) {
                                showAlert(response.body().getMessage());

                                PreferenceUtils.setString(
                                        Constant.PreferenceConstant.USER_DATA,
                                        new Gson().toJson(response.body().getUsers().get(0)),
                                        EditProfileActivity.this);

                                getPreferenceData();

                                if (isFrom.equals(Constant.OTP_SCREEN)) {
                                    PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGGED_IN, true, EditProfileActivity.this);
                                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(EditProfileActivity.this, SelectStateActivity.class);
                                    startActivity(intent);
                                }
                                finishAffinity();


                            }
                        } else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void setData() {

//        if (userModel.getmUserImage() != null) {
//            Glide.with(UpdateUserActivity.this)
//                    .load(ImagePathDecider.getCustomerImagePath() + userModel.getmUserImage())
//                    .error(R.drawable.ic_user_img)
//                    .into(binding.ivUser);
//        }

        if (userModel.getmUserName() != null) {
            binding.etName.setText(userModel.getmUserName());
        }

        if (userModel.getmUserEmail() != null) {
            binding.etEmail.setText(userModel.getmUserEmail());
        }

        if (userModel.getmUserDob() != null) {
            binding.etDob.setText(Utils.changeDateFormat(Constant.yyyyMMdd, Constant.ddMMMyyyy, userModel.getmUserDob()));
        }

        if (userModel.getmUserAddress() != null) {
            binding.etAddress.setText(userModel.getmUserAddress());
        }

//        if (userModel.getmUserPincode() != null) {
//            binding.etPincode.setText(userModel.getmUserPincode());
//        }
    }

    private void openDatePickerDialog(EditText editText) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                editText.setText(Utils.getDate(myCalendar.getTimeInMillis(), Constant.ddMMMyyyy));
                dob = Utils.getDate(myCalendar.getTimeInMillis(), Constant.yyyyMMdd);
            }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void isDataUpdated() {
        boolean isUpdated = false;

        if (!binding.etName.getText().toString().equals(userModel.getmUserName())) {
            isUpdated = true;
        } else if (!binding.etEmail.getText().toString().equals(userModel.getmUserEmail())) {
            isUpdated = true;
        } else if (!binding.etDob.getText().toString().equals(userModel.getmUserDob())) {
            isUpdated = true;
//        } else if (!binding.etPincode.getText().toString().equals(userModel.getmUserPincode())) {
//            isUpdated = true;
        } else if (!binding.etAddress.getText().toString().equals(userModel.getmUserAddress())) {
            isUpdated = true;
        }

        if (isUpdated) {
            binding.btnSave.setEnabled(true);
        } else {
            binding.btnSave.setEnabled(false);
        }

    }

    private boolean validate() {
        boolean valid = true;
        if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError("Please Enter Your Name");
            valid = false;
        }

        return valid;
    }

}