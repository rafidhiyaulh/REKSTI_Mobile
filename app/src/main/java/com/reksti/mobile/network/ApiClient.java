package com.reksti.mobile.network;

import android.content.Context;

import com.reksti.mobile.utils.SharedPrefManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            String baseUrl = SharedPrefManager.getInstance(context).getBaseUrl();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
