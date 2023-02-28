package com.practices.alebeer.data.model.response

sealed class ErrorResponse : Throwable() {
    class ApiError : ErrorResponse()
}
