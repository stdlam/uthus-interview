package com.practices.alebeer.core.di

import com.practices.alebeer.core.base.BaseApplication
import com.practices.alebeer.data.local.database.createDatabase
import com.practices.alebeer.data.local.database.createSampleDao
import com.practices.alebeer.data.remote.provider.Providers
import com.practices.alebeer.data.remote.repo.MainRepo
import com.practices.alebeer.data.remote.repo_imp.MainRepoImp
import com.practices.alebeer.data.remote.retrofit.MainRetrofit
import com.practices.alebeer.ui.main.MainViewModel
import com.practices.alebeer.ui.main.beer.BeerViewModel
import com.practices.alebeer.ui.main.favorite.FavViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    //===== Application =====
    single { BaseApplication.instance }

    //===== Local Database =====
    single { createDatabase(get()) }
    single { createSampleDao(get()) }

    //===== Network =====
    single { MainRetrofit(get()) }
    single { Providers.getMainProvider(get()) }

    //===== Repositories =====
    single<MainRepo> { MainRepoImp(get(), get()) }
}

val appModule = module {
    viewModel { FavViewModel(get()) }
    viewModel { BeerViewModel(get()) }
    viewModel { MainViewModel(get()) }
}
