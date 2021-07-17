package com.xxjy.common.base

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.xxjy.common.R

/**
 * Created by Administrator on 2016/5/5.
 */
@RequiresApi(Build.VERSION_CODES.CUPCAKE)
abstract class BaseDialog protected constructor(
    context: Context?, gravity: Int, marginVerticle: Float,
    animationDirection: AnimationDirection, backCancelable: Boolean,
    outsideCancelable: Boolean
) : Dialog(
    context!!, R.style.BaseDialog
) {
    enum class AnimationDirection {
        HORIZONTAL, VERTICLE, GROW
    }

    var view: View

    /**
     * 创建dialog 的方法, 省略了一些参数,只需要
     *
     * @param context           上下文
     * @param gravity           对齐方式
     * @param backCancelable    返回键是否可以取消
     * @param outsideCancelable 外部点击是否可以取消
     */
    protected constructor(
        context: Context?,
        gravity: Int,
        backCancelable: Boolean,
        outsideCancelable: Boolean
    ) : this(context, gravity, 0.0f, AnimationDirection.GROW, backCancelable, outsideCancelable) {
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun init(
        gravity: Int,
        marginVerticle: Float,
        animationDirection: AnimationDirection,
        backCancelable: Boolean,
        outsideCancelable: Boolean
    ) {
        setCancelable(backCancelable)
        setCanceledOnTouchOutside(outsideCancelable)
        val dialogWindow = this.window
        if (animationDirection == AnimationDirection.VERTICLE) {
            dialogWindow!!.setWindowAnimations(R.style.DialogVerticleWindowAnim)
        } else if (animationDirection == AnimationDirection.HORIZONTAL) {
            dialogWindow!!.setWindowAnimations(R.style.DialogRightHorizontalWindowAnim)
        } else if (animationDirection == AnimationDirection.GROW) {
            dialogWindow!!.setWindowAnimations(R.style.DialogGrowWindowAnim)
        }
        dialogWindow!!.decorView.setPadding(0, 0, 0, 0)
        val layoutParams: WindowManager.LayoutParams = dialogWindow.attributes
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = gravity
        layoutParams.verticalMargin = marginVerticle
        initLayout(layoutParams)
        dialogWindow.attributes = layoutParams
    }

    /**
     * 设置宽 ,高 ,位置, 距离底部的方法, 可以由子类进行重写
     *
     * @param layoutParams
     */
    protected open fun initLayout(layoutParams: WindowManager.LayoutParams?) {
        //todo default is :layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //todo default is :layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //todo can use :layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //todo can use :layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    override fun dismiss() {
        releaseData()
        super.dismiss()
    }

    /**
     * 在弹窗即将关闭的时候释放数据操作,由子类进行操作
     */
    protected fun releaseData() {}

    //设置 resoucelayoutid
    protected abstract val contentLayoutId: Int

    /**
     * 创建dialog的方法
     *
     * @param context            上下文
     * @param gravity            对齐方式
     * @param marginVerticle     底部的距离,一般为 0
     * @param animationDirection 动画效果 [AnimationDirection]
     * @param backCancelable     返回键是否可以取消
     * @param outsideCancelable  外部点击是否可以取消
     */
    init {
        init(gravity, marginVerticle, animationDirection, backCancelable, outsideCancelable)
        view = LayoutInflater.from(context).inflate(contentLayoutId, null, false)
        setContentView(view)
    }
}