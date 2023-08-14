package com.sis.clighteningboost.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private object UnSecureTrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
}


fun getUnsafeOkHttpClient(context: Context): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(UnSecureTrustManager)

    val sslContext: SSLContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory


    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }.addInterceptor(
            ChuckerInterceptor.Builder(context).build()
        ).addNetworkInterceptor(httpLoggingInterceptor).build()
}