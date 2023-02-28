package com.practices.alebeer.helper.constant

enum class Error(val code: Int) {
    API_ERROR(CALL_ERROR)
}

enum class ErrorType {
    TOKEN_EXPIRED,
    NO_INTERNET,
    CONNECT_EXCEPTION,
    OTHER
}

enum class ApiSuccess(val code: Int) {
    HAS_DATA(SUCCESS)
}