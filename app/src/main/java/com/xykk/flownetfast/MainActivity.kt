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
import net.flow.jetpackmvvm.ext.download.DownLoadManager
import net.flow.jetpackmvvm.ext.download.OnDownLoadListener
import net.flow.jetpackmvvm.ext.download.downLoadExt
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
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    download()
                }
            }
        }
    }

    private suspend fun download() {
        DownLoadManager.downLoad("app",this,"https://b7804be5a071519a58e18b7b947061cb.rdt.tfogc.com:49156/imtt.dd.qq.com/sjy.10001/16891/apk/DCEBC567E14F1F22830246522767CB1F.apk?mkey=626dc69809ddbed17c1b0cfc8f928780&arrive_key=735430190809&fsname=com.dl.schedule_2.0.2_7.apk&csr=3554&cip=223.104.150.117&proto=https","sdcard/Download/apk","testDownload.apk",reDownload = true, loadListener = object:OnDownLoadListener{
            override fun onDownLoadPrepare(key: String) {
                key.loge("download")
            }

            override fun onDownLoadError(key: String, throwable: Throwable) {
                "$key --- ${throwable.message}".loge("download")
            }

            override fun onDownLoadSuccess(key: String, path: String, size: Long) {
                "$key --- $path --- $size".loge("download")
                ToastUtils.showLong("下载成功")
            }

            override fun onDownLoadPause(key: String) {
            }

            override fun onUpdate(
                key: String,
                progress: Int,
                read: Long,
                count: Long,
                done: Boolean
            ) {
                "$key --- progress = $progress --- done: $done".loge("download")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}