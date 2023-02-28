package com.practices.alebeer.core.remote

import com.practices.alebeer.helper.extension.asyncRemoteData
import com.practices.alebeer.helper.extension.asyncDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Response

open class BaseRepositoryImp {
    fun asyncLocalData(
        query: suspend CoroutineScope.(Int) -> Unit,
        onSuccess: ((Boolean) -> Unit)? = null,
        onError: (Throwable?) -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.Default).asyncDatabase(
            query,
            onSuccess = {
                onSuccess?.invoke(it)
            },
            onError = { onError(it) }
        )
    }

    fun <T> asyncRemoteData(
        apiDeferred: Deferred<Response<T>>,
        onSuccess: (T) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).asyncRemoteData(
            apiDeferred,
            onSuccess = { onSuccess(it) },
            onError = { onError(it) },
        )
    }
}