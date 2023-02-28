package com.practices.alebeer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun getNetwork(context: Context?) = context?.let { ctx ->
    (ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
} ?: false