package com.logixhunt.shikhaaqua.ui.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.databinding.ActivitySplashBinding;
import com.logixhunt.shikhaaqua.model.UserModel;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;
import com.logixhunt.shikhaaqua.utils.PreferenceUtils;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;
    private String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
    }

    private void initialization() {
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                log_e(TAG, "", task.getException());
                return;
            }
            String notificationToken = task.getResult();
            log_d(TAG, "token=====>" + notificationToken);
            PreferenceUtils.setString(
                    Constant.PreferenceConstant.FIREBASE_TOKEN,
                    notificationToken,
                    SplashActivity.this
            );
        });

        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        }

        checkUpdate();
//        startApp();
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (!PreferenceUtils.getBoolean(Constant.PreferenceConstant.FIRST_LAUNCH_COMPLETE, SplashActivity.this)) {
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                } else {
                    if (PreferenceUtils.getBoolean(Constant.PreferenceConstant.IS_LOGGED_IN, SplashActivity.this)) {
                        if (PreferenceUtils.getString(Constant.PreferenceConstant.STATE_ID, SplashActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.STATE_ID, SplashActivity.this).isEmpty() && PreferenceUtils.getString(Constant.PreferenceConstant.CITY_ID, SplashActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.CITY_ID, SplashActivity.this).isEmpty() && PreferenceUtils.getString(Constant.PreferenceConstant.AREA_ID, SplashActivity.this) != null && !PreferenceUtils.getString(Constant.PreferenceConstant.AREA_ID, SplashActivity.this).isEmpty()) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        } else {

                            intent = new Intent(SplashActivity.this, SelectStateActivity.class);
                        }
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void checkUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashActivity.this);


        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());

            }else {
                startApp();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "App update check failed: " + e.getMessage());
            startApp();
        });

    }
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // handle callback
                    int resultCode = result.getResultCode();
                    if (resultCode != RESULT_OK) {
                        Log.d(TAG, "Update flow failed! Result code: " + resultCode);
                        // If the update is cancelled or fails,
                        // you can request to start the update again.
                        showAlertDialog();
                    }else {
                        showAlert("App Updated Successfully");
                        startApp();
                    }
                }
            });
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setMessage("Please install the latest version to continue using the app");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        AlertDialog dialog = builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkUpdate();
            }
        }).create();
        dialog.show();

    }

}