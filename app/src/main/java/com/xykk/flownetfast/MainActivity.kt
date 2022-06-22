package com.xykk.flownetfast

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mmkv.MMKV
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
import net.flow.jetpackmvvm.ext.download.*
import net.flow.jetpackmvvm.ext.parseState
import net.flow.jetpackmvvm.ext.request
import net.flow.jetpackmvvm.ext.requestGlobal
import net.flow.jetpackmvvm.ext.util.downloadManager
import net.flow.jetpackmvvm.ext.util.loge
import net.flow.jetpackmvvm.network.manager.NetState
import net.flow.jetpackmvvm.state.ResultState
import java.io.File

/**
 *@description 网络请求测试
 *@author wangkeke
 *@date 2022/4/12 4:20 下午
 */
class MainActivity : BaseActivity<RequestMainViewModel, ActivityMainBinding>(){

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
//            requestMainViewModel.postWebSiteRequest()
//            requestMainViewModel.postWebSiteRequestNoCheck(lifecycleScope)
//            requestMainViewModel.postWebSiteRequestOther(lifecycleScope)
            //测试全局请求
            requestGlobal({ apiService.website()},{
                mDatabind.tvData.text = it.toString()
            }, showLoading = true)
        }

        mDatabind.btnDownload.setOnClickListener {
            MyApp.instance().applicationScope.launch {
                download()
            }
        }

        mDatabind.btnTest.setOnClickListener {
            MainScope().launch {
                withContext(Dispatchers.IO){
                    val result = getLong("testApp", 0L)
                    "获取当前key的值：${result}".loge()
                }
            }
        }
    }

    private suspend fun download() {
        DownLoadManager.downLoad("testApp",this,"https://cos.pgyer.com/6ce2b1e072ca2a306fbe3d1061de0764.apk?sign=cc01e4b318326e365ee993fab725e506&t=1655867098&response-content-disposition=attachment%3Bfilename%3DFlowNetFast_1.1.apk",cacheDir!!.absolutePath,"testDownload.apk",reDownload = true, loadListener = object:OnDownLoadListener{
            override fun onDownLoadPrepare(key: String) {
                key.loge("download")
            }

            override fun onDownLoadError(key: String, throwable: Throwable) {
                "$key --- ${throwable.message}".loge("download")
            }

            override fun onDownLoadSuccess(key: String, path: String, size: Long) {
                "$key --- $path --- $size".loge("download")
                ToastUtils.showLong("下载成功")
//                AppUtils.installApp(File(path))
                runOnUiThread {
                    mDatabind.tvData.text = "下载完成！"
                }
            }

            override fun onDownLoadPause(key: String) {
                "$key----pause".loge("download")
            }

            override fun onUpdate(
                key: String,
                progress: Int,
                read: Long,
                count: Long,
                done: Boolean
            ) {
                "$key --- progress = $progress --- done: $done".loge("download")
                runOnUiThread {
                    mDatabind.tvData.text = "当前下载进度：$progress%"
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}