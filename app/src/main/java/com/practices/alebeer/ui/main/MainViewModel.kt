package com.practices.alebeer.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.practices.alebeer.core.base.BaseViewModel
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.data.remote.repo.MainRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepo: MainRepo) : BaseViewModel() {
    private val _favSampleResponse = MutableSharedFlow<MutableList<ItemBeerLocal>>()
    val favSampleResponse = _favSampleResponse.asSharedFlow()

    private val favData: MutableList<ItemBeerLocal> = mutableListOf()

    fun getFavSampleValues() = favData

    fun getFavSamples() {
        showLoading()
        mainRepo.getFavSampleData(
            onSuccess = {
                Log.d("XXX", "getFavSampleData $it")
                viewModelScope.launch {
                    _favSampleResponse.emit(it)
                    favData.clear()
                    favData.addAll(it)
                }
                dismissLoading()
            },
            onError = {
                handleError(it)
                dismissLoading()
            }
        )
    }
}