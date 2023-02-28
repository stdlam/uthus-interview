package com.practices.alebeer.data.remote.retrofit

import android.app.Application
import com.practices.alebeer.core.remote.BaseRetrofit
import com.practices.alebeer.helper.constant.BASE_URL

class MainRetrofit(
    application: Application
) : BaseRetrofit(application) {

    override fun requestBaseUrl(): String = BASE_URL

}