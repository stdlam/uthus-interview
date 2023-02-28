package com.practices.alebeer.data.remote.api

import com.practices.alebeer.core.remote.BaseResponseModel
import com.practices.alebeer.data.model.response.SampleResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
    @GET("/api/api-testing/sample-data")
    fun getSampleDataAsync(@Query("page") page: Int, @Query("limit") limit: Int): Deferred<Response<BaseResponseModel<ArrayList<SampleResponse>>>>
}