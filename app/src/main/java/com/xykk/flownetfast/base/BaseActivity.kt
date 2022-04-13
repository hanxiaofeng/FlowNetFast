package com.xykk.flownetfast.base

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import net.flow.jetpackmvvm.base.activity.BaseVmDbActivity
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    /**
     * 当前Activity绑定的视图布局Id abstract修饰供子类实现
     */
    abstract override fun layoutId(): Int
    /**
     * 当前Activityc创建后调用的方法 abstract修饰供子类实现
     */
    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {

    }

    /**
     * 打开等待框 在这里实现你的等待框展示
     */
    override fun showLoading(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 关闭等待框 在这里实现你的等待框关闭
     */
    override fun dismissLoading() {
        Toast.makeText(this@BaseActivity, "dimiss", Toast.LENGTH_SHORT).show()
    }
}