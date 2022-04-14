package net.flow.jetpackmvvm.base.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.getVmClazz
import net.flow.jetpackmvvm.ext.util.notNull
import net.flow.jetpackmvvm.network.manager.NetState
import net.flow.jetpackmvvm.network.manager.NetworkStateManager

/**
 * 描述　: ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    private var isFullScreen = false

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    /**
     *@description onCreate super之前相关代码放在此方法内处理
     *@user wangkeke
     *@time 2022/4/13 1:55 下午
     */
    abstract fun onCreateBefore()

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun showLoading(custom: Boolean = false, message: String = "加载中···")

    abstract fun dismissLoading(custom: Boolean = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore()
        super.onCreate(savedInstanceState)
        initDataBind().notNull({
            setContentView(it)
        },{
            setContentView(layoutId())
        })
        init(savedInstanceState)
        onClick()
    }

    /**
     * 点击事件
     */
    open fun onClick() {

    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            showLoading(message = it)
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel){
        viewModels.forEach {viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observe(this, Observer {
                showLoading(message = it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observe(this, Observer {
                dismissLoading()
            })
        }
    }

    fun setFullScreen(isFullScreen: Boolean) {
        this.isFullScreen = isFullScreen
    }

    fun isFullScreen():Boolean{
        return isFullScreen
    }

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind():View? {
        return null
    }
}