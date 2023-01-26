package com.sis.clighteningboost.Api;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientForNode {
    public static final String NEW_BASE_URL = "http://73.36.65.41:8000/";

    public static final String LOCAL_BASE_URL = "";
    public static Retrofit retrofit = null;
    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            new ChuckerInterceptor.Builder(context)
                                    .build()
                    )
                    .addNetworkInterceptor(httpLoggingInterceptor)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(NEW_BASE_URL).client(httpClient).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
