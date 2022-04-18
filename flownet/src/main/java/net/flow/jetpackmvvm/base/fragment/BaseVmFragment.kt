package net.flow.jetpackmvvm.base.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.flow.jetpackmvvm.base.viewmodel.BaseViewModel
import net.flow.jetpackmvvm.ext.getVmClazz
import net.flow.jetpackmvvm.ext.launchWithScope
import net.flow.jetpackmvvm.network.manager.NetState
import net.flow.jetpackmvvm.network.manager.NetworkStateManager

/**
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment
 */

abstract class BaseVmFragment<VM : BaseViewModel> : Fragment() {

    private val handler = Handler(Looper.getMainLooper()!!)

    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        registorDefUIChange()
        initData()
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
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                lifecycleScope.launch {
                    NetworkStateManager.instance.mNetworkStateCallback.collect {
                        it?.let {
                            //不是首次订阅时调用方法，防止数据第一次监听错误
                            if (!isFirst) {
                                onNetworkStateChanged(it)
                            }
                        }
                    }
                }
                isFirst = false
            }, lazyLoadTime())
        }
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}

    abstract fun showLoading(custom: Boolean = false, message: String = "请求网络中...")

    abstract fun dismissLoading(custom: Boolean = false)

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        launchWithScope {
            //隐藏弹窗
            mViewModel.loadingChange.dismissDialog.collect {
                dismissLoading()
            }
        }
        launchWithScope {
            //隐藏弹窗
            mViewModel.loadingChange.dismissDialog.collect {
                dismissLoading()
            }
        }
    }

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            launchWithScope {
                //显示弹窗
                viewModel.loadingChange.showDialog.collect {
                    showLoading()
                }
            }
            launchWithScope {
                //隐藏弹窗
                viewModel.loadingChange.dismissDialog.collect {
                    dismissLoading()
                }
            }
        }
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}