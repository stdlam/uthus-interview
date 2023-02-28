package com.practices.alebeer.data.remote.provider

import com.practices.alebeer.data.remote.api.MainApi
import com.practices.alebeer.data.remote.retrofit.MainRetrofit

object Providers {
    fun getMainProvider(retrofit: MainRetrofit): MainApi {
        return retrofit.retrofit.create(MainApi::class.java)
    }
}