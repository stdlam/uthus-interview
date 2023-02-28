package com.practices.alebeer.data.model.response

import com.google.gson.annotations.SerializedName
import com.practices.alebeer.core.base.BaseModel

data class SampleResponse(
    val id: Int,
    val price: String? = null,
    val name: String? = null,
    val rating: SampleRatingResponse? = null,
    val image: String? = null,
    @SerializedName("sale_off_time")
    val saleOffTime: Long? = null
) : BaseModel()
