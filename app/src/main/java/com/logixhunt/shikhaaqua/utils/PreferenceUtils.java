package com.logixhunt.shikhaaqua.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class PreferenceUtils {

    private static final String DEFAULT_PREFS = "default_shared_prefs";
    private static final int DEFAULT_NUMERIC_VALUE = 0;
    private static final String DEFAULT_STRING_VALUE = "";

    public static void setString(String key, @Nullable String value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(key, DEFAULT_STRING_VALUE);
    }

    public static void setBoolean(String key, boolean value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void setInteger(String key, int value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInteger(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(key, DEFAULT_NUMERIC_VALUE);
    }

    /**
     * removes particular key (and its associated value) from preferences
     */
    public static void removeKey(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * clears all key-value pairs in shared preferences
     */
    public static void clearAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

}
