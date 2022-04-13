package net.flow.jetpackmvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.util.dismissLoadingExt
import net.flow.jetpackmvvm.util.showLoadingExt

/**
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmFragment<VM>() {

    //该类绑定的ViewDataBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDatabind = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDatabind.lifecycleOwner = this
        return mDatabind.root
    }

}