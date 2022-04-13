package com.xykk.flownetfast

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.xykk.flownetfast.base.BaseActivity
import com.xykk.flownetfast.databinding.ActivityMainBinding
import com.xykk.flownetfast.viewmodel.request.RequestMainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.flow.jetpackmvvm.ext.util.loge

/**
 *@description 网络请求测试
 *@author wangkeke
 *@date 2022/4/12 4:20 下午
 */
class MainActivity : BaseActivity<RequestMainViewModel,ActivityMainBinding>() {

    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    private fun testSharedFlow(){
        val sharedFlow = MutableSharedFlow<String>()

        lifecycleScope.launch {
            sharedFlow.emit("张三")
            sharedFlow.emit("刘二")
            delay(1000)
            sharedFlow.emit("李四")
            delay(1000)
            sharedFlow.emit("王五")
            delay(1000)
            sharedFlow.emit("哈哈哈")
        }

        lifecycleScope.launch {
            sharedFlow.collect {
                "shareFlow user1: $it, thread:${Thread.currentThread().name}".loge("flowTest")
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            sharedFlow.collect {
                "shareFlow user2 : $it, thread:${Thread.currentThread().name}".loge("flowTest")
            }
        }
    }

    private fun testFlow() {
        val flowProduce = flow<String> {
            emit("张三")
            delay(1000)
            emit("李四")
            delay(1000)
            emit("王五")
            delay(1000)
            emit("哈哈哈")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            flowProduce.collect {
                "$it, thread:${Thread.currentThread().name}".loge("flowTest")
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            flowProduce.collect {
                "user2 : $it, thread:${Thread.currentThread().name}".loge("flowTest")
            }
        }
    }

    override fun createObserver() {
        requestMainViewModel.websiteResult.observe(this) {
            dismissLoading()
            it.toString().loge()
            mDatabind.tvData.text = it.toString()
        }
    }

    override fun onClick() {
        mDatabind.btnPostNet.setOnClickListener {
            showLoading("请求数据中~")
            requestMainViewModel.postWebSiteRequest()
//            testSharedFlow()
        }
    }

}