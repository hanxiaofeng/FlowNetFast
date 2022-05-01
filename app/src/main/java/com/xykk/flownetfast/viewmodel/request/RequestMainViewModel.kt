package com.xykk.flownetfast.viewmodel.request

import com.xykk.flownetfast.model.UsuallyWebSites
import com.xykk.flownetfast.network.ApiResponse
import com.xykk.flownetfast.network.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.ext.requestNoCheck
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.state.ResultState

/**
 * 专门用于请求数据的viewModel
 */
class RequestMainViewModel: BaseViewModel() {

    private val _websiteResult = MutableStateFlow<ResultState<UsuallyWebSites>>(ResultState.onEmpty())
    val websiteResult:StateFlow<ResultState<UsuallyWebSites>> = _websiteResult


    private val _websiteResultNoCheck = MutableStateFlow<ResultState<ApiResponse<UsuallyWebSites>>>(ResultState.onEmpty())
    val websiteResultNoCheck:StateFlow<ResultState<ApiResponse<UsuallyWebSites>>> = _websiteResultNoCheck


    fun postWebSiteRequest(){
        request({ apiService.website()},_websiteResult,true)
    }

    fun postWebSiteRequestNoCheck(scope: CoroutineScope){
        requestNoCheck({ apiService.website()},_websiteResultNoCheck,true)
    }

    fun postWebSiteRequestOther(scope: CoroutineScope){
        request({ apiService.website()},{
            it.toString().loge()
        },{
            it.message?.loge()
        },true,"")
    }
}