package com.xxjy.common.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup
import com.xxjy.common.R
import com.xxjy.common.databinding.DialogNotificeTipsLayoutBinding

/**
 * @author power
 * @date 1/22/21 5:59 PM
 * @project ElephantOil
 * @description:
 */
class NoticeTipsDialog(context: Context) : QMUIFullScreenPopup(context) {
    private val mBinding: DialogNotificeTipsLayoutBinding
    private fun init() {
        addView(mBinding.root)
        closeBtn(false)
        skinManager(QMUISkinManager.defaultInstance(mContext))
        mBinding.queryTv.setOnClickListener {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onQueryClick()
            }
        }
        mBinding.closeIv.setOnClickListener {
//            if (mOnItemClickedListener != null) {
//                mOnItemClickedListener.onQueryClick();
//            }
            dismiss()
        }
        mBinding.noOpenView.setOnClickListener(View.OnClickListener {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener!!.onNoOpen()
            }
            dismiss()
        })
    }

    interface OnItemClickedListener {
        fun onQueryClick()
        fun onNoOpen()
    }

    private var mOnItemClickedListener: OnItemClickedListener? = null
    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener?) {
        mOnItemClickedListener = onItemClickedListener
    }

    init {
        this.mContext = context
        mBinding = DialogNotificeTipsLayoutBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.dialog_notifice_tips_layout, null)
        )
        init()
    }
}