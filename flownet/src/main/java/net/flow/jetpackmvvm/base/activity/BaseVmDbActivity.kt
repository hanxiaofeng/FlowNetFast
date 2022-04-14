package net.flow.jetpackmvvm.base.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.inflateBindingWithGeneric
import net.flow.jetpackmvvm.util.dismissLoadingExt
import net.flow.jetpackmvvm.util.showLoadingExt

/**
 * 描述　: 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId() = 0

    lateinit var mDatabind: DB

    override fun showLoading(custom: Boolean, message: String) {
        if(!custom){
            showLoadingExt()
        }
    }

    override fun dismissLoading(custom: Boolean) {
        if(!custom){
            dismissLoadingExt()
        }
    }

    /**
     * 创建DataBinding
     */
    override fun initDataBind():View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}