package com.sis.clighteningboost.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    //http://104.128.189.40/api/
   // public static final String NEW_BASE_URL = "http://boostterminal.stepinnsolution.com/api/";
   // public static final String NEW_BASE_URL = "http://104.128.189.40/boostterminal/api/";
    public static final String NEW_BASE_URL = "https://mainframe.nextlayer.live/api/";
    //public static final String NEW_BASE_URL = "https://boostterminal.nextlayer.live/api/";


    public static final String LOCAL_BASE_URL = "";
    public static Retrofit retrofit = null;
    public static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(50,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
        출처: https://3edc.tistory.com/52 [Three SAL is sol sol]
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(NEW_BASE_URL).client(httpClient).addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
