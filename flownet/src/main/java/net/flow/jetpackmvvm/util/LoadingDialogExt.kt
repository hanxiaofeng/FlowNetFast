package net.flow.jetpackmvvm.util

import android.app.Activity
import android.app.Dialog
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.github.ybq.android.spinkit.SpinKitView
import net.flow.jetpackmvvm.R

//loading框
private var loadingDialog: Dialog? = null
/**
 * 打开等待框
 */
fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = Dialog(this, R.style.loadstyle)
            val dialogView: View = LayoutInflater.from(this).inflate(
                R.layout.loading_view, null
            )
            loadingDialog?.setCanceledOnTouchOutside(false)
            loadingDialog?.setCancelable(false)
            loadingDialog?.setContentView(dialogView)
            val dialogWindow: Window = loadingDialog?.window!!
            dialogWindow.setGravity(
                Gravity.CENTER_VERTICAL
                        or Gravity.CENTER_HORIZONTAL
            )
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
        }
        if (!(loadingDialog?.isShowing)!!) {
            loadingDialog?.show()
        }
    }
}

/**
 * 打开等待框
 */
fun Fragment.showLoadingExt(message: String = "请求网络中") {
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog == null) {
                loadingDialog = Dialog(it, R.style.loadstyle)
                val dialogView: View = LayoutInflater.from(it).inflate(
                    R.layout.loading_view, null
                )
                loadingDialog?.setCanceledOnTouchOutside(false)
                loadingDialog?.setCancelable(false)
                loadingDialog?.setContentView(dialogView)
                val dialogWindow: Window = loadingDialog?.window!!
                dialogWindow.setGravity(
                    Gravity.CENTER_VERTICAL
                            or Gravity.CENTER_HORIZONTAL
                )
                dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
            }
            if (!(loadingDialog?.isShowing)!!) {
                loadingDialog?.show()
            }        }
    }
}

/**
 * 全局的打开等待框
 */
fun showLoadingExt(message: String = "加载中") {
    val topActivity = ActivityUtils.getTopActivity()
    if (!topActivity.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = Dialog(topActivity, R.style.loadstyle)
            val dialogView: View = LayoutInflater.from(topActivity).inflate(
                R.layout.loading_view, null
            )
            loadingDialog?.setCanceledOnTouchOutside(false)
            loadingDialog?.setCancelable(false)
            loadingDialog?.setContentView(dialogView)
            val dialogWindow: Window = loadingDialog?.window!!
            dialogWindow.setGravity(
                Gravity.CENTER_VERTICAL
                        or Gravity.CENTER_HORIZONTAL
            )
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
        }
        if (!(loadingDialog?.isShowing)!!) {
            loadingDialog?.show()
        }
    }
}

/**
 * 关闭等待框
 */
fun Activity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

/**
 * 关闭等待框
 */
fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

/**
 * 关闭等待框
 */
fun dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
