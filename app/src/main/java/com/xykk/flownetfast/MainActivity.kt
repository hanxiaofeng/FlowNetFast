package com.xykk.flownetfast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.blankj.utilcode.util.ToastUtils
import com.xykk.flownetfast.base.BaseActivity
import com.xykk.flownetfast.databinding.ActivityMainBinding
import com.xykk.flownetfast.model.UsuallyWebSites
import com.xykk.flownetfast.network.apiService
import com.xykk.flownetfast.viewmodel.request.RequestMainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import net.flow.jetpackmvvm.ext.parseState
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.ext.requestGlobal
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.network.manager.NetState
import net.flow.jetpackmvvm.state.ResultState

/**
 *@description 网络请求测试
 *@author wangkeke
 *@date 2022/4/12 4:20 下午
 */
class MainActivity : BaseActivity<RequestMainViewModel, ActivityMainBinding>(),
    CoroutineScope by MainScope() {

    private var postJob:Job? = null

    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onNetworkStateChanged(netState: NetState) {
        netState.isSuccess.toString().loge("netState")
    }

    override fun createObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                requestMainViewModel.websiteResult.collect {
                    "Main: "+Thread.currentThread().name.loge("wangkeke")
                    parseState(it,{ websites ->
                        websites.toString().loge()
                        mDatabind.tvData.text = websites.toString()
                    },{ error ->
                        error.message?.loge()
                    })
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                requestMainViewModel.websiteResultNoCheck.collect {
                    parseState(it,{ result ->
                        if(result.isSucces()){
                            mDatabind.tvData.text = result.data.toString()
                        }else{
                            ToastUtils.showLong("error:${result.message}")
                        }
                    },{ error ->
                        error.message?.loge()
                    })
                }
            }
        }
    }

    override fun onClick() {
        mDatabind.btnPostNet.setOnClickListener {
            //测试普通请求
            requestMainViewModel.postWebSiteRequest(lifecycleScope)
//            requestMainViewModel.postWebSiteRequestNoCheck(lifecycleScope)
//            requestMainViewModel.postWebSiteRequestOther(lifecycleScope)
            //测试全局请求
//            postJob = requestGlobal({ apiService.website()},{
//                mDatabind.tvData.text = it.toString()
//            }, showLoading = true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        postJob?.cancel()
    }
}