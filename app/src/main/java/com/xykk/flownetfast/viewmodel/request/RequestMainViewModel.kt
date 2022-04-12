package com.xykk.flownetfast.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.xykk.flownetfast.model.UsuallyWebSites
import com.xykk.flownetfast.network.apiService
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.state.ResultState

/**
 * 专门用于请求数据的viewModel
 */
class RequestMainViewModel: BaseViewModel() {

    var websiteResult : MutableLiveData<ResultState<UsuallyWebSites>> = MutableLiveData()

    fun postWebSiteRequest(){
        request({ apiService.website()},websiteResult)
    }

}