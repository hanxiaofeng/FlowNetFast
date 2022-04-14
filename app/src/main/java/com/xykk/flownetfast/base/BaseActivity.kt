package com.xykk.flownetfast.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import net.flow.jetpackmvvm.base.activity.BaseVmDbActivity
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    /**
     * 当前Activityc创建后调用的方法 abstract修饰供子类实现
     */
    abstract override fun initView(savedInstanceState: Bundle?)

    override fun onCreateBefore() {
//        var uiFlags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN)
//        uiFlags = uiFlags or 0x00001000
//        window.decorView.systemUiVisibility = uiFlags
//        val window = window
//        val params = window.attributes
//        params.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
//        window.attributes = params
    }

    override fun createObserver() {

    }

    /**
     * 打开等待框 在这里实现你的等待框展示，否则使用默认提供样式
     */
    override fun showLoading(custom:Boolean, message: String) {
        super.showLoading(custom, message)
        //注释掉super.showLoading,在这里实现你的等待框展示，否则使用默认提供样式
    }

    /**
     * 关闭等待框 在这里实现你的等待框关闭，否则使用默认提供样式
     */
    override fun dismissLoading(custom:Boolean) {
        super.dismissLoading(custom)
        //注释掉super.dismissLoading,在这里实现你的等待框关闭，否则使用默认提供样式
    }
}