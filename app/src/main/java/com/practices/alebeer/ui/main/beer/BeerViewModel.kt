package com.practices.alebeer.ui.main.beer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.practices.alebeer.core.base.BaseModel
import com.practices.alebeer.core.base.BaseViewModel
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.data.remote.repo.MainRepo
import com.practices.alebeer.helper.constant.PAGE_LOAD_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL

class BeerViewModel(private val mainRepo: MainRepo) : BaseViewModel() {
    private val _sampleResponse = MutableSharedFlow<RemoteDataWrapper?>()
    val sampleResponse = _sampleResponse.asSharedFlow()

    private val _saveItemResponse = MutableSharedFlow<LocalSavingWrapper?>()
    val saveItemResponse = _saveItemResponse.asSharedFlow()

    private val _infinityPageCountFlow = MutableStateFlow<Int?>(null)
    val infinityPageCountFlow = _infinityPageCountFlow.asSharedFlow()

    data class LocalSavingWrapper(
        var savedItem: ItemBeerLocal? = null,
        var isSaved: Boolean? = null
    ) : BaseModel()

    data class RemoteDataWrapper(
        var data: MutableList<ItemBeerLocal>? = null,
        var canLoadMore: Boolean = false,
        var loadedMore: Boolean = false,
        var isRefresh: Boolean = false
    ) : BaseModel()

    fun fetchRemoteSampleData(page: Int, localData: ArrayList<ItemBeerLocal>, isRefresh: Boolean = false, isLoadMore: Boolean = false) {
        mainRepo.getRemoteSampleData(
            page = page,
            limit = PAGE_LOAD_LIMIT,
            onSuccess = {
                Log.d("XXX", "getSampleData - data=$it")
                _infinityPageCountFlow.tryEmit(it.total / PAGE_LOAD_LIMIT)
                val localBeers = mutableListOf<ItemBeerLocal>()
                it.data.forEach { response ->
                    val isItemExisted = localData.findLast { localItem -> localItem.id == response.id } != null
                    localBeers.add(
                        ItemBeerLocal(
                            id = response.id,
                            avatarUrl = response.image ?: "",
                            avatarPath = "",
                            name = response.name ?: "",
                            price = response.price ?: "",
                            note = "",
                            isFav = false,
                            isFreezeNote = isItemExisted,
                            isSaving = false,
                            saleOffTime = response.saleOffTime
                        )
                    )
                }
                viewModelScope.launch {
                    _sampleResponse.emit(RemoteDataWrapper(
                        data = localBeers,
                        canLoadMore = it.loadMore,
                        isRefresh = isRefresh,
                        loadedMore = isLoadMore
                    ))
                }
            }, onError = {
                handleError(it)
                dismissLoading()
            })
    }

    fun saveItem(context: Context, itemBeerLocal: ItemBeerLocal) {
        viewModelScope.launch {
            itemBeerLocal.avatarPath = saveImage(context, itemBeerLocal)
            mainRepo.saveFavSample(
                itemBeerLocal.convertToEntity(),
                onSuccess = {
                    viewModelScope.launch {
                        _saveItemResponse.emit(
                            LocalSavingWrapper(
                                itemBeerLocal,
                                true
                            )
                        )
                    }
                },
                onError = {
                    viewModelScope.launch {
                        _saveItemResponse.tryEmit(
                            LocalSavingWrapper(
                                itemBeerLocal,
                                false
                            )
                        )
                    }
                    handleError(it)
                }
            )
        }
    }

    private suspend fun saveImage(context: Context, itemBeerLocal: ItemBeerLocal): String {
        var imagePath = ""
        withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(itemBeerLocal.avatarUrl).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val imageFileName = "ale_beer" + "${itemBeerLocal.name}_${itemBeerLocal.id}.jpg"
                val storageDir = File(context.filesDir, "AleBeer/images")

                var success = true
                if (!storageDir.exists()) {
                    success = storageDir.mkdirs()
                }
                if (success) {
                    val imageFile = File(storageDir, imageFileName)
                    imagePath = imageFile.absolutePath
                    try {
                        val fOut: OutputStream = FileOutputStream(imageFile)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                        fOut.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                Log.d("XXX", "saveImage - storageDir=$storageDir, imagePath=$imagePath")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return imagePath
    }
}