package com.xykk.flownetfast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.xykk.flownetfast.base.BaseActivity
import com.xykk.flownetfast.databinding.ActivityMainBinding
import com.xykk.flownetfast.databinding.ActivityNetRequestBinding
import com.xykk.flownetfast.model.UsuallyWebSites
import com.xykk.flownetfast.network.apiService
import com.xykk.flownetfast.viewmodel.request.RequestMainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.ext.requestGlobal
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.state.ResultState

/**
 *@description 网络请求测试
 *@author wangkeke
 */
class NetRequestActivity : BaseActivity<RequestMainViewModel, ActivityNetRequestBinding>() {

    private var job:Job? = null

    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {
        requestMainViewModel.websiteResult.observe(this) {
            "接收到网络请求结果了：$it".loge()
            mDatabind.tvData.text = it.toString()
        }
    }

    override fun onClick() {
        mDatabind.btnPostNet.setOnClickListener {
            //测试普通请求
//            requestMainViewModel.postWebSiteRequest()
            //测试全局请求
            job = requestGlobal({ apiService.website()},{
                "global--接收到网络请求结果了：$it".loge()
                mDatabind.tvData.text = it.toString()
            }, showLoading = false)
        }

        mDatabind.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}