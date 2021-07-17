package com.xxjy.common.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.xxjy.common.R
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.databinding.DialogHomeNewUserLayoutBinding
import com.xxjy.common.entity.HomeNewUserBean
import com.xxjy.common.provide.MContext.context
import com.xxjy.common.util.GlideUtils.loadImage

open class HomeNewUserDialog private constructor() {
    private var mContext: Context? = null
    private var mPopup: QMUIFullScreenPopup? = null
    private val mBinding: DialogHomeNewUserLayoutBinding = DialogHomeNewUserLayoutBinding.bind(
        LayoutInflater.from(context()).inflate(R.layout.dialog_home_new_user_layout, null)
    )
    private var mBean: HomeNewUserBean? = null

    open fun setData(context: Context, bean: HomeNewUserBean?) {
        mContext = context
        mBean = bean
        init()
    }

    private fun init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
            .addView(mBinding.root)
            .closeBtn(false)
            .skinManager(QMUISkinManager.defaultInstance(mContext))
        mBinding.closeIv.setOnClickListener { v: View? ->
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onCloseClick(v)
            }
            dismiss()
        }
        loadImage(mContext!!, mBean!!.newUrl, mBinding.adImg)
        mBinding.adImg.setOnClickListener { //                NaviActivityInfo.disPathIntentFromUrl((BaseActivity) mContext,mBean.getH5Link());
            dismiss()
        }
    }

    fun show(view: View?) {
        if (mPopup != null) {
            if (!UserConstants.isNewUserRedPacket()) {
                mPopup!!.show(view)
                UserConstants.new_user_red_packet
            }
        }
    }

    fun dismiss() {
        if (mPopup != null) {
            mPopup!!.dismiss()
        }
    }

    interface OnItemClickedListener {
        fun onCloseClick(view: View?)
    }

    private var mOnItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: () -> Unit) {
        onItemClickedListener.also { mOnItemClickedListener }
    }

    companion object {
        //有调用者直接就拿出来给了
        //提前创建一个Singleton
        @SuppressLint("StaticFieldLeak")
        val instance = HomeNewUserDialog()
    }

}