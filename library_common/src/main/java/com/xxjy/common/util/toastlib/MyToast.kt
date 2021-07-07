package com.xxjy.common.util.toastlib

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2017/9/12.
 *
 *
 * 这是一个 管理 toast 弹窗的工具类
 */
object MyToast {
    private var mToast: Toast? = null
    private val mHandler = Handler(Looper.getMainLooper())

    /**
     * 显示一个普通的toast
     *
     * @param context
     * @param text
     */
    fun show(context: Context?, text: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        }
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.setText(text)
        mToast!!.show()
    }

    /**
     * 显示一个普通的长时间toast
     *
     * @param context
     * @param text
     */
    fun showLong(context: Context?, text: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        }
        mToast!!.duration = Toast.LENGTH_LONG
        mToast!!.setText(text)
        mToast!!.show()
    }

    /**
     * 屏幕中心展示一个toast
     *
     * @param context
     * @param myToastShow
     */
    fun showContentToast(context: Context?, myToastShow: String?) {
        showNormal(context, myToastShow)
    }

    /**
     * 展示 普通的 toast
     *
     * @param context
     * @param myToastShow
     */
    fun showNormal(context: Context?, myToastShow: String?) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_NORMAL)
    }

    /**
     * 展示 信息级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    fun showInfo(context: Context?, myToastShow: String?) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_INFO)
    }

    /**
     * 展示 警告级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    fun showWarning(context: Context?, myToastShow: String?) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_WARNING)
    }

    /**
     * 展示 成功级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    fun showSuccess(context: Context?, myToastShow: String?) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_SUCCESS)
    }

    /**
     * 展示 错误级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    fun showError(context: Context?, myToastShow: String?) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_ERROR)
    }

    /**
     * 通过不同的类型来展示不同的级别的toast
     *
     * @param context
     * @param myToastShow
     * @param toastyType
     */
    fun showLeavelToast(context: Context?, myToastShow: String?, toastyType: ToastType?) {
        when (toastyType) {
            ToastType.TYPE_NORMAL -> runOnUiThread {
                Toasty.normal(
                    context!!, myToastShow!!
                )?.show()
            }
            ToastType.TYPE_INFO -> runOnUiThread { Toasty.info(context!!, myToastShow!!)?.show() }
            ToastType.TYPE_WARNING -> runOnUiThread {
                Toasty.warning(context!!, myToastShow!!)?.show()
            }
            ToastType.TYPE_SUCCESS -> runOnUiThread {
                Toasty.success(context!!, myToastShow!!)?.show()
            }
            ToastType.TYPE_ERROR -> runOnUiThread { Toasty.error(context!!, myToastShow!!)?.show() }
        }
    }

    private fun runOnUiThread(runnable: Runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run()
        } else {
            mHandler.post(runnable)
        }
    }
}