package com.xykk.flownetfast.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import net.flow.jetpackmvvm.base.fragment.BaseVmDbFragment
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *@description fragment基类
 *@author wangkeke
 *@date 2022/4/12 1:58 下午
 */
abstract class BaseFragment<VM : BaseViewModel,DB: ViewDataBinding> : BaseVmDbFragment<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法 abstract修饰供子类实现
     */
    abstract override fun lazyLoadData()

    override fun createObserver() {

    }

    /**
     * Fragment执行onViewCreated后触发的方法
     */
    override fun initData() {

    }

    /**
     * 打开等待框 在这里实现你的等待框展示
     */
    override fun showLoading(message: String) {

    }

    /**
     * 关闭等待框 在这里实现你的等待框关闭
     */
    override fun dismissLoading() {

    }
}