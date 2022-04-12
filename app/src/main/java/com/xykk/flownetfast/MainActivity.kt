package com.xykk.flownetfast

import android.os.Bundle
import androidx.activity.viewModels
import com.xykk.flownetfast.base.BaseActivity
import com.xykk.flownetfast.databinding.ActivityMainBinding
import com.xykk.flownetfast.viewmodel.request.RequestMainViewModel
import net.flow.jetpackmvvm.ext.util.loge

/**
 *@description 网络请求测试
 *@author wangkeke
 *@date 2022/4/12 4:20 下午
 */
class MainActivity : BaseActivity<RequestMainViewModel,ActivityMainBinding>() {

    private val requestMainViewModel:RequestMainViewModel by viewModels()

    override fun layoutId(): Int {
       return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        postNet()
    }

    override fun createObserver() {
        requestMainViewModel.websiteResult.observe(this){
            dismissLoading()
            it.toString().loge()
            mDatabind.tvData.text = it.toString()
        }
    }

    private fun postNet() {
        showLoading("请求数据中~")
        mDatabind.btnPostNet.setOnClickListener {
            requestMainViewModel.postWebSiteRequest()
        }
    }

}