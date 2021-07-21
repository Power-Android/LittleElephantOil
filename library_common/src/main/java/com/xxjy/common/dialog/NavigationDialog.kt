package com.xxjy.common.dialog

import android.animation.Animator
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.xxjy.common.R
import com.xxjy.common.databinding.DialogNavigationBinding
import com.xxjy.common.util.MapIntentUtils
import per.goweii.anylayer.AnyLayer
import per.goweii.anylayer.Layer
import per.goweii.anylayer.dialog.DialogLayer
import per.goweii.anylayer.utils.AnimatorHelper

/**
 * @author power
 * @date 12/7/20 7:35 PM
 * @project RunElephant
 * @description:
 */
class NavigationDialog(
    context: Context,
    latitude: Double,
    longtitude: Double,
    endName: String
) {
    private var mContext: Context = context
    private lateinit var mDialogLayer: DialogLayer
    private val mBinding: DialogNavigationBinding
    private val latitude: Double
    private val longtitude: Double
    private val endName: String
    private fun init() {
        mDialogLayer = AnyLayer.dialog(mContext)
            .contentView(mBinding.root)
            .cancelableOnClickKeyBack(true)
            .cancelableOnTouchOutside(true)
            .gravity(Gravity.BOTTOM)
            .contentAnimator(object : Layer.AnimatorCreator {
                override fun createInAnimator(target: View): Animator? {
                    return AnimatorHelper.createBottomInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator? {
                    return AnimatorHelper.createBottomOutAnim(target)
                }
            })
        if (MapIntentUtils.isPhoneHasMapGaoDe) {
            mBinding.mapGaode.setVisibility(View.VISIBLE)
            mBinding.lineGaode.setVisibility(View.VISIBLE)
        }
        if (MapIntentUtils.isPhoneHasMapBaiDu) {
            mBinding.mapBaidu.setVisibility(View.VISIBLE)
            mBinding.lineBaidu.setVisibility(View.VISIBLE)
        }
        if (MapIntentUtils.isPhoneHasMapTencent) {
            mBinding.mapTencent.setVisibility(View.VISIBLE)
            mBinding.lineTencent.setVisibility(View.VISIBLE)
        }
        mDialogLayer!!.onClick(Layer.OnClickListener { layer: Layer, view: View ->
            when (view.id) {
                R.id.cancle -> layer.dismiss()
                R.id.map_gaode -> MapIntentUtils.openGaoDe(mContext, latitude, longtitude, endName)
                R.id.map_baidu -> MapIntentUtils.openBaidu(mContext, latitude, longtitude, endName)
                R.id.map_tencent -> MapIntentUtils.openTencent(
                    mContext,
                    latitude,
                    longtitude,
                    endName
                )
            }
            layer.dismiss()
        }, R.id.cancle, R.id.map_gaode, R.id.map_baidu, R.id.map_tencent)
    }

    fun show() {
        if (mDialogLayer != null && !mDialogLayer.isShown()) {
            mDialogLayer.show()
        }
    }

    init {
        this.latitude = latitude
        this.longtitude = longtitude
        this.endName = endName
        mBinding = DialogNavigationBinding.bind(
            LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_navigation, null, false)
        )
        init()
    }
}