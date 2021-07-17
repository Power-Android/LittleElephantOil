package com.xxjy.common.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.xxjy.common.R
import com.xxjy.common.databinding.DialogHomeAdLayoutBinding
import com.xxjy.common.entity.BannerBean
import com.xxjy.common.util.GlideUtils

class HomeAdDialog(private val mContext: Context, bannerBean: BannerBean) {
    private var mPopup: QMUIFullScreenPopup? = null
    private val mBinding: DialogHomeAdLayoutBinding = DialogHomeAdLayoutBinding.bind(
        LayoutInflater.from(mContext).inflate(R.layout.dialog_home_ad_layout, null)
    )
    private val mBannerBean: BannerBean = bannerBean
    private fun init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
            .addView(mBinding.root)
            .closeBtn(false)
            .skinManager(QMUISkinManager.defaultInstance(mContext))
        mBinding.closeIv.setOnClickListener { v ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onCloseClick(v)
            }
            dismiss()
        }
        GlideUtils.loadImage(mContext, mBannerBean.imgUrl, mBinding.adImg)
        mBinding.adImg.setOnClickListener(View.OnClickListener {
            //TODO lata
//            NaviActivityInfo.disPathIntentFromUrl(
//                mContext as BaseActivity,
//                mBannerBean.getLink()
//            )
        })
    }

    fun show(view: View?) {
        mPopup?.show(view)
    }

    fun dismiss() {
        mPopup?.dismiss()
    }

    interface OnItemClickedListener {
        fun onCloseClick(view: View?)
    }

    private var mOnItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: () -> Unit) {
        onItemClickedListener.also { mOnItemClickedListener }
    }

    init {
        init()
    }
}