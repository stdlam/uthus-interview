package com.practices.alebeer.core.remote

import android.app.Application
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofit(application: Application) {
    companion object {
        private const val TIME_OUT = 30L
        private const val MAX_CACHE = 10L * 1024 * 1024
    }

    abstract fun requestBaseUrl(): String

    private val builder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .cache(Cache(application.cacheDir, MAX_CACHE))
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
    }

    private val _retrofit by lazy {
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        Retrofit.Builder()
            .baseUrl(requestBaseUrl()).client(builder.build())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient().create()
                )
            ).build()
    }

    val retrofit: Retrofit
        get() = _retrofit

    fun addTokenAuthenticator(tokenHolder: Authenticator) = apply {
        builder.authenticator(tokenHolder)
    }

    fun addInterceptors(interceptor: Interceptor) = apply {
        builder.addInterceptor(interceptor)
    }
}