package com.logixhunt.shikhaaqua.utils;

import com.logixhunt.shikhaaqua.BuildConfig;

public class ImagePathDecider {

    public static String getProductImagePath() {
        return BuildConfig.BASE_IMG_URL + "bottle/";
    }

    public static String getBannerImagePath() {
        return BuildConfig.BASE_IMG_URL + "slider/";
    }
}
