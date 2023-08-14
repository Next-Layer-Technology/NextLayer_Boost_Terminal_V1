package com.sis.clighteningboost.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private fun trustManagers(): Array<TrustManager> = arrayOf(object : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
})

private fun httpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun sslSocketFactory(trustAllCerts: Array<TrustManager>): SSLSocketFactory {
    val sslContext: SSLContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    return sslContext.socketFactory
}


fun Context.okHttpClient(): OkHttpClient = commonOkHttpClientBuilder().build()

fun Context.okHttpClientTimeout180(): OkHttpClient = commonOkHttpClientBuilder()
    .readTimeout(180, TimeUnit.SECONDS)
    .connectTimeout(180, TimeUnit.SECONDS)
    .build()

private fun Context.commonOkHttpClientBuilder() = OkHttpClient.Builder()
    .sslSocketFactory(sslSocketFactory(trustManagers()), trustManagers()[0] as X509TrustManager)
    .hostnameVerifier { _, _ -> true }
    .addInterceptor(ChuckerInterceptor.Builder(this).build())
    .addNetworkInterceptor(httpLoggingInterceptor())