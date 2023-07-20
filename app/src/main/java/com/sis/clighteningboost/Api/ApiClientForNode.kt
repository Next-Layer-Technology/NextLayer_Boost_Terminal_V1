package com.sis.clighteningboost.Api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientForNode {
    const val NEW_BASE_URL = "http://73.36.65.41:8000/"
    const val LOCAL_BASE_URL = ""
    var retrofit: Retrofit? = null
    fun getRetrofit(context: Context): Retrofit? {
        if (retrofit == null) {
            val gson = GsonBuilder().setLenient().create()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .build()
                )
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build()
            retrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
        }
        return retrofit
    }
}