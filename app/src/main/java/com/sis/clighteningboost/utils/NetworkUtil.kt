package com.sis.clighteningboost.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sis.clighteningboost.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

private fun httpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun sslSocketFactory(trustManager: CustomTrustManager): SSLSocketFactory {
    val sslContext: SSLContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf(trustManager), SecureRandom())

    return sslContext.socketFactory
}


fun Context.okHttpClient(): OkHttpClient = commonOkHttpClientBuilder().build()

fun Context.okHttpClientTimeout180(): OkHttpClient = commonOkHttpClientBuilder()
    .readTimeout(180, TimeUnit.SECONDS)
    .connectTimeout(180, TimeUnit.SECONDS)
    .build()

private fun Context.commonOkHttpClientBuilder(): OkHttpClient.Builder {
    val caCertificateInputStream: InputStream = resources.openRawResource(R.raw.certificate)
    val trustManager = CustomTrustManager(caCertificateInputStream)
    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory(trustManager), trustManager)
        .hostnameVerifier { _, _ -> true }
        .addInterceptor(ChuckerInterceptor.Builder(this).build())
        .addNetworkInterceptor(httpLoggingInterceptor())
}