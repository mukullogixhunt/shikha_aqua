package com.logixhunt.shikhaaqua.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.logixhunt.shikhaaqua.BuildConfig;
import com.logixhunt.shikhaaqua.api.ApiClient;
import com.logixhunt.shikhaaqua.api.ApiInterface;
import com.logixhunt.shikhaaqua.api.response.commonresponse.BaseResponse;
import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.activities.LoginActivity;
import com.logixhunt.shikhaaqua.ui.activities.MainActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;
import com.logixhunt.shikhaaqua.widgets.CustomProgressDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {


    Dialog mProgressDialog;
    public static BaseActivity baseActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;

        //progress dialog
        mProgressDialog = new CustomProgressDialog(this);


    }

    /**
     * show loader
     */
    public void showLoader() {
        try {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Hide Loader
     */
    public void hideLoader() {
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Show Error
     */
    public void showError(String msg) {
        if (msg == null) return;
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }


    public UserModel getUserData(Context context) {
        UserModel user = new UserModel();
        String userJson = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, context);
        user = new Gson().fromJson(userJson, UserModel.class);
        return user;
    }

    /**
     * Show alert
     */
    public void showAlert(String msg) {
        if (msg == null) return;
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void log_d(String className, String message) {
        if (BuildConfig.DEBUG)
            Log.d(className, "" + message);
    }

    public void log_e(String className, String message, Exception e) {
        if (BuildConfig.DEBUG)
            Log.e(className, "" + message, e);
    }

    public void showAlertDialog(String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        AlertDialog dialog = builder.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).create();
        dialog.show();

    }

    public void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Check Email is valid or not
     *
     * @param email - email
     * @return - true if email is valid else false
     */
    public boolean isEmailValid(String email) {
        String regex =
                "[A-Z0-9a-z]+([._%+-]{1}[A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void appShare(String message) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}
