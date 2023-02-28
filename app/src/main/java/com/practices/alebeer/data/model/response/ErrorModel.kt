package com.practices.alebeer.data.model.response

import com.practices.alebeer.core.base.BaseModel
import com.practices.alebeer.helper.constant.ErrorType

data class ErrorModel(
    val errorType: ErrorType = ErrorType.OTHER,
    val milliSecond: String = "",
    val message: String = ""
) : BaseModel()
