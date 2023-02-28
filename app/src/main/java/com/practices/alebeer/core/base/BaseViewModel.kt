package com.practices.alebeer.core.base

import android.os.SystemClock
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practices.alebeer.data.model.response.ErrorModel
import com.practices.alebeer.data.model.response.ErrorResponse
import com.practices.alebeer.getNetwork
import com.practices.alebeer.helper.constant.ErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException

open class BaseViewModel : ViewModel() {
    private val _onLoading = MutableStateFlow(false)
    val onLoading = _onLoading.asStateFlow()

    private val _onError = MutableStateFlow(ErrorModel())
    val onError = _onError.asStateFlow()

    fun showLoading() {
        _onLoading.value = true
    }

    fun dismissLoading() {
        _onLoading.value = false
    }

    private fun setError(exception: String?, errorType: ErrorType = ErrorType.OTHER) {
        _onError.value = ErrorModel(
            milliSecond = SystemClock.currentThreadTimeMillis().toString(),
            message = exception ?: "",
            errorType = errorType
        )
    }

    fun handleError(e: Throwable?) {
        dismissLoading()
        e?.let {
            when (it) {
                is ErrorResponse.ApiError -> {}
                is ConnectException -> {
                    if (!getNetwork(BaseApplication.instance))
                        setError("No Internet", ErrorType.NO_INTERNET)
                    else setError("ConnectException", ErrorType.NO_INTERNET)
                }
                else -> setError(e.message ?: "")
            }
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }
}