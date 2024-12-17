package com.logixhunt.shikhaaqua.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.logixhunt.shikhaaqua.model.UserModel;


public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * to show error messages only in Snackbar
     */
    protected void showError(String msg) {
        ((BaseActivity) getActivity()).showError(msg);
    }

    /**
     * Show Loader
     */
    protected void showLoader() {
        try {
            ((BaseActivity) getActivity()).showLoader();
        } catch (Exception e) {

        }
    }

    public UserModel getUserData(Context context) {
        return ((BaseActivity) getActivity()).getUserData(context);
    }


    /**
     * Show alert
     */
    public void showToast(String msg) {
        ((BaseActivity) getActivity()).showAlert(msg);
    }

    public void log_d(String className, String message) {
        ((BaseActivity) getActivity()).log_d(className, message);
    }

    public void log_e(String className, String message, Exception e) {
        ((BaseActivity) getActivity()).log_e(className, message, e);
    }

    /**
     * Hide Loader
     */
    protected void hideLoader() {
        try {
            ((BaseActivity) getActivity()).hideLoader();
        } catch (Exception e) {

        }
    }

    public void hideKeyBoard(View view) {
        ((BaseActivity) getActivity()).hideKeyBoard(view);
    }

}
