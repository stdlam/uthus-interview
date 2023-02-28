package com.practices.alebeer.helper.extension

import com.practices.alebeer.data.model.response.ErrorResponse
import kotlinx.coroutines.*
import retrofit2.Response

fun CoroutineScope.asyncDatabase(
    query: suspend CoroutineScope.(Int) -> Unit,
    onSuccess: (Boolean) -> Unit,
    onError: (Throwable?) -> Unit
): Job {

    return launch {
        try {
            query(1)
            onSuccess(true)
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun <T> CoroutineScope.asyncRemoteData(
    apiDeferred: Deferred<Response<T>>,
    onSuccess: (T) -> Unit,
    onError: (Throwable?) -> Unit
) {
    launch(Dispatchers.IO) {
        try {
            apiDeferred.await().run {
                when {
                    isSuccessful -> body()?.let { onSuccess(it) }
                    else ->
                        onError(
                            when (code()) {
                                com.practices.alebeer.helper.constant.Error.API_ERROR.code -> ErrorResponse.ApiError()
                                else -> Throwable(
                                    "${code()} - ${
                                        if (errorBody() != null) errorBody()?.string() else
                                            message()
                                    }"
                                )
                            }
                        )
                }
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}