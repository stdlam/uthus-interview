package com.practices.alebeer.data.model.response

import com.practices.alebeer.core.base.BaseModel

data class SampleRatingResponse(
    val average: Float? = null,
    val reviews: Int? = null
) : BaseModel()
