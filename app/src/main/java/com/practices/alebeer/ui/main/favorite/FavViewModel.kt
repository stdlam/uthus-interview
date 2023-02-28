package com.practices.alebeer.ui.main.favorite

import androidx.lifecycle.viewModelScope
import com.practices.alebeer.core.base.BaseViewModel
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.data.remote.repo.MainRepo
import kotlinx.coroutines.launch

class FavViewModel(private val mainRepo: MainRepo) : BaseViewModel() {

    fun deleteSample(itemBeerLocal: ItemBeerLocal) {
        showLoading()
        mainRepo.deleteFavSample(
            itemBeerLocal.id,
            onSuccess = {
                dismissLoading()
            },
            onError = {
                handleError(it)
                dismissLoading()
            }
        )
    }

    fun updateItem(itemBeerLocal: ItemBeerLocal) {
        viewModelScope.launch {
            mainRepo.updateFavSample(
                itemBeerLocal.convertToEntity(),
                onSuccess = { },
                onError = {
                    handleError(it)
                }
            )
        }
    }
}