package com.reksti.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "reksti_prefs";
    private static final String KEY_BASE_URL = "base_url";

    public static void setBaseUrl(Context context, String baseUrl) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_BASE_URL, baseUrl).apply();
    }

    public static String getBaseUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_BASE_URL, "https://reksti-face-api-e7fwfqfzb4f3gtgf.eastus2-01.azurewebsites.net/");
    }
}
