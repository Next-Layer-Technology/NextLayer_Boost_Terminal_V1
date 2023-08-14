package com.sis.clighteningboost.Api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    //https://104.128.189.40/api/
    // public static final String NEW_BASE_URL = "https://boostterminal.stepinnsolution.com/api/";
    // public static final String NEW_BASE_URL = "https://104.128.189.40/boostterminal/api/";
    const val NEW_BASE_URL = "https://mainframe.nextlayer.live/api/"

    //public static final String NEW_BASE_URL = "https://boostterminal.nextlayer.live/api/";
    const val LOCAL_BASE_URL = ""
    var retrofit: Retrofit? = null
    fun getRetrofit(context: Context): Retrofit? {
        val gson = GsonBuilder().setLenient().create()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(
               ChuckerInterceptor. Builder(context)
                    .build()
            )
            .build()
        출처@ https@ //3edc.tistory.com/52 [Three SAL is sol sol]
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }
}