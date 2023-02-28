package com.practices.alebeer.core.base

import android.app.Application
import com.practices.alebeer.core.di.appModule
import com.practices.alebeer.core.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    companion object {
        lateinit var instance: BaseApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initialKoin()
    }

    private fun initialKoin() {
        startKoin {
            androidContext(instance)
            modules(
                listOf(
                    dataModule,
                    appModule
                )
            )
        }
    }
}