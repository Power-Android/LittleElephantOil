package com.xxjy.common.base

import android.animation.Animator
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.xxjy.common.R
import com.xxjy.common.util.toastlib.MyToast
import per.goweii.anylayer.AnyLayer
import per.goweii.anylayer.Layer.AnimatorCreator
import per.goweii.anylayer.dialog.DialogLayer
import per.goweii.anylayer.utils.AnimatorHelper

abstract class BaseActivity : QMUIFragmentActivity() {

    private var mLoadingDialog: DialogLayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoadingDialog()
        BarUtils.setStatusBarLightMode(this, true)
    }

    /**
     * 设置透明状态栏-默认为深色
     */
    fun setTransparentStatusBar() {
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, true)
    }

    /**
     * @param view toolbar设置margin
     *
     * 设置透明状态栏的margin
     */
    fun setTransparentStatusBar(view: View?) =
        setTransparentStatusBar(view, true)

    /**
     * @param isDark 默认为深色
     *
     * 设置透明状态栏的颜色
     */
    open fun setTransparentStatusBar(view: View?, isDark: Boolean) {
        BarUtils.transparentStatusBar(this)
        if (view != null) {
            BarUtils.addMarginTopEqualStatusBarHeight(view)
        }
        BarUtils.setStatusBarLightMode(this, isDark)
    }

    open fun showToast(msg: String?) =
        MyToast.showContentToast(this, msg)

    open fun showToastInfo(msg: String?) =
        MyToast.showInfo(this, msg)

    open fun showToastWarning(msg: String?) =
        MyToast.showWarning(this, msg)

    open fun showToastError(msg: String?) =
        MyToast.showError(this, msg)

    open fun showToastSuccess(msg: String?) =
        MyToast.showSuccess(this, msg)

    private fun initLoadingDialog() {
        mLoadingDialog = AnyLayer.dialog()
            .contentView(R.layout.loading_layout)
            .cancelableOnClickKeyBack(true)
            .gravity(Gravity.CENTER)
            .contentAnimator(object : AnimatorCreator {
                override fun createInAnimator(target: View): Animator? {
                    return AnimatorHelper.createAlphaInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator? {
                    return AnimatorHelper.createAlphaOutAnim(target)
                }
            })
            .cancelableOnTouchOutside(false)
    }

    /**
     * 展示读取的 dialog
     */
    open fun showLoadingDialog() = mLoadingDialog?.show()

    /**
     * 隐藏读取的 dialog
     */
    open fun dismissLoadingDialog() = mLoadingDialog?.dismiss()

}