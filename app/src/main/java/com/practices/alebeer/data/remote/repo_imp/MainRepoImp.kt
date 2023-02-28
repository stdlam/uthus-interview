package com.practices.alebeer.data.remote.repo_imp

import com.practices.alebeer.core.remote.BaseRepositoryImp
import com.practices.alebeer.core.remote.BaseResponseModel
import com.practices.alebeer.data.local.database.dao.SampleDao
import com.practices.alebeer.data.local.database.entity.SampleEntity
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.data.model.response.SampleResponse
import com.practices.alebeer.data.remote.api.MainApi
import com.practices.alebeer.data.remote.repo.MainRepo

class MainRepoImp(private val mainApi: MainApi, private val sampleDao: SampleDao) : BaseRepositoryImp(), MainRepo {
    override fun getRemoteSampleData(
        page: Int,
        limit: Int,
        onSuccess: (BaseResponseModel<ArrayList<SampleResponse>>) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        asyncRemoteData(
            mainApi.getSampleDataAsync(page, limit),
            onSuccess = {
                onSuccess(it)
            },
            onError = {
                onError(it)
            }
        )
    }

    override fun getFavSampleData(
        onSuccess: (ArrayList<ItemBeerLocal>) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        asyncLocalData(
            query = {
                sampleDao.getFavSamples()?.map {
                    it.convertToBeerItem()
                }?.let {
                    onSuccess(ArrayList(it))
                }
            },
            onError = {
                onError(it)
            }
        )
    }

    override fun saveFavSample(
        entity: SampleEntity,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        asyncLocalData(
            query = {
                sampleDao.insertSamples(listOf(entity))
            },
            onSuccess = {
                onSuccess(it)
            },
            onError = {
                onError(it)
            }
        )
    }

    override fun deleteFavSample(
        id: Int,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        asyncLocalData(
            query = {
                sampleDao.deleteSample(id)
            },
            onSuccess = {
                onSuccess(it)
            },
            onError = {
                onError(it)
            }
        )
    }

    override fun updateFavSample(
        entity: SampleEntity,
        onSuccess: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        asyncLocalData(
            query = {
                sampleDao.updateSample(entity)
            },
            onSuccess = {
                onSuccess(it)
            },
            onError = {
                onError(it)
            }
        )
    }
}