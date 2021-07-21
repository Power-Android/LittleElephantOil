package com.xxjy.common.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.xxjy.common.R
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.databinding.DialogCallHelpBinding
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.vm.AboutUsViewModel

class CustomerServiceDialog(private val mContext: Context) {
    private lateinit var mPopup: QMUIFullScreenPopup
    private val mBinding: DialogCallHelpBinding = DialogCallHelpBinding.bind(
        LayoutInflater.from(mContext).inflate(R.layout.dialog_call_help, null)
    )
    private var mPhone: String? = null
    private var mLink: String? = null
    private var mTime: String? = null
    private val aboutUsViewModel: AboutUsViewModel =
        ViewModelProvider(mContext as BaseActivity).get(
            AboutUsViewModel::class.java
        )

    private fun init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
            .addView(mBinding.getRoot())
            .closeBtn(false)
            .skinManager(QMUISkinManager.defaultInstance(mContext))
        mPopup.onBlankClick(QMUIFullScreenPopup.OnBlankClickListener { popup: QMUIFullScreenPopup? -> dismiss() })
        mBinding.callHelpOnline.setOnClickListener { v ->
            dismiss()
            toWebChatOnline(mContext, mLink)
        }
        mBinding.callHelpPhone.setOnClickListener { v ->
            dismiss()
            toDialPhoneAct(mContext, mPhone)
        }
        aboutUsViewModel.getCallCenter()
        aboutUsViewModel.callCenterLiveData.observe(mContext as BaseActivity) { data ->
            mLink = data.callOnline
            mPhone = data.callPhone
            mTime = data.callNotice
            mBinding.callHelpWorkTime.text = mTime
        }
    }

    fun show(view: View?) {
        if (mPopup != null) {
            mPopup.show(view)
        }
    }

    fun dismiss() {
        if (mPopup != null) {
            mPopup.dismiss()
        }
    }

    companion object {
        /**
         * 跳转拨打电话界面
         *
         * @param activity
         * @param phoneNumber
         * @return
         */
        fun toDialPhoneAct(activity: Context, phoneNumber: String?): Boolean {
            if (TextUtils.isEmpty(phoneNumber)) {
                return false
            }
            try {
                val phoneUri = Uri.parse("tel:$phoneNumber")
                val intent = Intent(Intent.ACTION_DIAL, phoneUri)
                if (intent.resolveActivity(activity.packageManager) != null) {
                    activity.startActivity(intent)
                    return true
                }
            } catch (e: Exception) {
            }
            (activity as BaseActivity).showToastWarning("您的设备无法拨打电话")
            return false
        }

        /**
         * 跳转在线客服
         *
         * @param activity
         * @param kFLink
         */
        fun toWebChatOnline(activity: Context, kFLink: String?) {
            if (TextUtils.isEmpty(kFLink)) {
                (activity as BaseActivity).showToastWarning("在线客服暂无法连接, 请选择其他方式")
                return
            }
            ARouterManager.navigation(RouteConstants.Web.A_WEB)
                .withString(RouteConstants.ParameterKey.URL, kFLink).navigation()

        }
    }

    init {
        init()
    }
}