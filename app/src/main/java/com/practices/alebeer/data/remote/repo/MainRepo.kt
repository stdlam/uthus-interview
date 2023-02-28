package com.practices.alebeer.data.remote.repo

import com.practices.alebeer.core.remote.BaseResponseModel
import com.practices.alebeer.data.local.database.entity.SampleEntity
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.data.model.response.SampleResponse

interface MainRepo {
    fun getRemoteSampleData(
        page: Int,
        limit: Int,
        onSuccess: (BaseResponseModel<ArrayList<SampleResponse>>) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun getFavSampleData(
        onSuccess: (ArrayList<ItemBeerLocal>) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun saveFavSample(
        entity: SampleEntity,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun deleteFavSample(
        id: Int,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun updateFavSample(
        entity: SampleEntity,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    )
}