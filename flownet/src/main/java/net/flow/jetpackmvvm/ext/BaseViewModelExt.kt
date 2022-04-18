package net.flow.jetpackmvvm.ext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import net.flow.jetpackmvvm.base.activity.BaseVmActivity
import net.flow.jetpackmvvm.base.fragment.BaseVmFragment
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.util.getFlow
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.network.AppException
import net.flow.jetpackmvvm.network.BaseResponse
import net.flow.jetpackmvvm.network.ExceptionHandle
import net.flow.jetpackmvvm.state.ResultState
import net.flow.jetpackmvvm.state.paresException
import net.flow.jetpackmvvm.state.paresResult
import net.flow.jetpackmvvm.util.dismissLoadingExt
import net.flow.jetpackmvvm.util.showLoadingExt

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * 注意：解析接口一定要使用这个方法，因为stateflow有默认值，未发送请求之前collect就会触发，需要单独处理下
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmActivity<*>.parseState(
    resultState: ResultState<T>?,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(message = resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
        else -> {}
    }
}

/**
 * data为null的情况处理，允许T？接收null，然后在具体回调中处理
 */
fun <T> BaseVmActivity<*>.parseStateCanNull(
    resultState: ResultState<T>?,
    onSuccess: (T?) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(message = resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
        else -> {}
    }
}


/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: ((message: String) -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            if (onLoading == null) {
                showLoading(message = resultState.loadingMessage)
            } else {
                onLoading.invoke(resultState.loadingMessage)
            }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}


/**
 * 全局的request，不依赖activity，请在非ui页面特殊情况下使用，不到万不得已不建议使用
 * 注意：记得在需要退出协程任务的时候主动退出
 * @param block 请求体方法
 * @param success 请求成功的回调
 * @param error 请求失败的回调
 */
fun <T> requestGlobal(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    showLoading: Boolean = false
): Job {
    return GlobalScope.launch {
        withContext(Dispatchers.Main) {
            if (showLoading) showLoadingExt()
        }
        getFlow {
            block()
        }
            .flowOn(Dispatchers.IO)
            .onEach {
                if (showLoading) {
                    dismissLoadingExt()
                }
                executeResponse(it) { t ->
                    success(t)
                }
            }.catch {
                if (showLoading) {
                    dismissLoadingExt()
                }
                it.printStackTrace()
                //失败回调
                error(ExceptionHandle.handleException(it))

            }.flowOn(Dispatchers.Main)
            .launchIn(this)
            .start()
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    scope: CoroutineScope,
    block: suspend () -> BaseResponse<T>,
    resultState: MutableStateFlow<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "加载中···"
): Job {
    return viewModelScope.launch {
        if (isShowDialog) loadingChange.showDialog.emit(loadingMessage)
        //.flowOn(Dispatchers.IO)
        getFlow {
            block()
        }.flowOn(Dispatchers.IO)
            .onEach {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                resultState.paresResult(it.getResponseData())
            }.catch {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                it.message?.loge()
                resultState.paresException(it)
            }.launchIn(scope)
            .start()
    }
}

/**
 * net request 不校验请求结果数据是否是成功,拿到原始数据
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    scope: CoroutineScope,
    block: suspend () -> T,
    resultState: MutableStateFlow<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "加载中···"
): Job {
    return viewModelScope.launch {
        if (isShowDialog) loadingChange.showDialog.emit(loadingMessage)
        getFlow {
            block()
        }.flowOn(Dispatchers.IO)
            .onEach {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                resultState.paresResult(it)
            }.catch {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                it.message?.loge()
                resultState.paresException(it)
            }.launchIn(scope)
            .start()
    }
}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    scope: CoroutineScope,
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "加载中···"
): Job {
    return viewModelScope.launch {
        switchMain(scope) {
            if (isShowDialog) loadingChange.showDialog.emit(loadingMessage)
        }
        getFlow {
            block()
        }.flowOn(Dispatchers.IO)
            .onEach {
                "sssssss: " + Thread.currentThread().name.loge("wangkeke")
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                executeResponse(it) { t ->
                    success(t)
                }
            }.catch {
                "sssssss: " + Thread.currentThread().name.loge("wangkeke")
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                it.message?.loge()
                error(ExceptionHandle.handleException(it))
            }.flowOn(Dispatchers.Main)
            .launchIn(scope)
            .start()
    }
}

/**
 *  不过滤请求结果
 * @param block 请求体 必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不给
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    scope: CoroutineScope,
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "加载中···"
): Job {
    return viewModelScope.launch {
        if (isShowDialog) loadingChange.showDialog.emit(loadingMessage)
        getFlow {
            block()
        }.flowOn(Dispatchers.IO)
            .onEach {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                success(it)
            }.catch {
                if (isShowDialog) loadingChange.dismissDialog.emit(false)
                it.message?.loge()
                error(ExceptionHandle.handleException(it))
            }.launchIn(scope)
            .start()
        }
}

/**
 * 请求结果过滤，判断请求服务器请求结果是否成功，不成功则会抛出异常
 */
suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        when {
            response.isSucces() -> {
                success(response.getResponseData())
            }
            else -> {
                throw AppException(
                    response.getResponseCode(),
                    response.getResponseMsg(),
                    response.getResponseMsg()
                )
            }
        }
    }
}

/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}

fun <T> switchMain(scope: CoroutineScope, block: suspend () -> T) {
    scope.launch {
        runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }
    }
}
