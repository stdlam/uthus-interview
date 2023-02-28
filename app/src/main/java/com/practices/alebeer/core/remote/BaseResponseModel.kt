package com.practices.alebeer.core.remote

import com.practices.alebeer.core.base.BaseModel

data class BaseResponseModel<T>(
    val data: T,
    val message: String,
    val status: String,
    val loadMore: Boolean,
    val total: Int
) : BaseModel()
