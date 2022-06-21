package com.xykk.flownetfast

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.xykk.flownetfast.base.BaseActivity
import com.xykk.flownetfast.databinding.ActivityMainBinding
import com.xykk.flownetfast.model.UsuallyWebSites
import com.xykk.flownetfast.network.apiService
import com.xykk.flownetfast.viewmodel.request.RequestMainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import net.flow.jetpackmvvm.ext.download.DownLoadManager
import net.flow.jetpackmvvm.ext.download.OnDownLoadListener
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.ext.requestGlobal
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.state.ResultState

/**
 *@description 网络请求测试
 *@author wangkeke
 *@date 2022/4/12 4:20 下午
 */
class MainActivity : BaseActivity<RequestMainViewModel, ActivityMainBinding>() {

    private var job:Job? = null

    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    private fun testSharedFlow() {
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
            mDatabind.tvData.text = it.toString()
        }
    }

    override fun onClick() {
        mDatabind.btnPostNet.setOnClickListener {
            startActivity(Intent(this@MainActivity,NetRequestActivity::class.java))
        }

        mDatabind.btnDownload.setOnClickListener {
            MyApp.instance.applicationScope.launch {
                DownLoadManager.downLoad("appTag","https://cos.pgyer.com/ddb78822cdf6745da4d681ac0a275425.apk?sign=f91acfd78e5304ff3300f4c6db261427&t=1655797656&response-content-disposition=attachment%3Bfilename%3D%E7%99%BE%E5%BA%A6%E8%BE%93%E5%85%A5%E6%B3%95%E5%8D%8E%E4%B8%BA%E7%89%88_8.2.8.111.apk",cacheDir!!.absolutePath,"test.apk",reDownload = false,object: OnDownLoadListener{
                    override fun onDownLoadPrepare(key: String) {
                        "onDownLoadPrepare key: $key".loge()
                    }

                    override fun onDownLoadError(key: String, throwable: Throwable) {
                        "onDownLoadError key: $key throwable: ${throwable.message}".loge()
                    }

                    override fun onDownLoadSuccess(key: String, path: String, size: Long) {
                        "onDownLoadSuccess key: $key path: $path size: $size".loge()
                    }

                    override fun onDownLoadPause(key: String) {
                        "onDownLoadPause key: $key".loge()
                    }

                    override fun onUpdate(
                        key: String,
                        progress: Int,
                        read: Long,
                        count: Long,
                        done: Boolean
                    ) {
                        "onUpdate key: $key progress: $progress".loge()
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}