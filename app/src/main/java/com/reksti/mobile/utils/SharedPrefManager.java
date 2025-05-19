package com.reksti.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "reksti_prefs";
    private static final String KEY_BASE_URL = "base_url";
    private static final String KEY_NIM = "nim";

    private static SharedPrefManager instance;
    private final SharedPreferences prefs;

    private SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void setBaseUrl(String baseUrl) {
        prefs.edit().putString(KEY_BASE_URL, baseUrl).apply();
    }

    public String getBaseUrl() {
        return prefs.getString(KEY_BASE_URL, "https://reksti-be-test-production.up.railway.app/");
    }

    public void setNIM(String nim) {
        prefs.edit().putString(KEY_NIM, nim).apply();
    }

    public String getNIM() {
        return prefs.getString(KEY_NIM, "");
    }
}
