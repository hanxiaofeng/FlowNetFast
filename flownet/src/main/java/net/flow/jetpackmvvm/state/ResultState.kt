package net.flow.jetpackmvvm.state
import kotlinx.coroutines.flow.MutableStateFlow
import net.flow.jetpackmvvm.network.AppException
import net.flow.jetpackmvvm.network.BaseResponse
import net.flow.jetpackmvvm.network.ExceptionHandle

/**
 * 描述　: 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)
        fun <T> onEmpty(): ResultState<T> = Empty()
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
    class Empty : ResultState<Nothing>()
}

/**
 * 异常转换异常处理
 */
fun <T> MutableStateFlow<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onAppError(ExceptionHandle.handleException(e))
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableStateFlow<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onAppSuccess(result)
}