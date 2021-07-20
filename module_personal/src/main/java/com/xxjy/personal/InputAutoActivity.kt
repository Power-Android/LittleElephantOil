package com.xxjy.personal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.*
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.weight.MyCountDownTime
import com.xxjy.common.weight.autocodeedittextview.VerificationCodeInput
import com.xxjy.personal.databinding.ActivityInputAutoBinding
import com.xxjy.personal.viewmodel.LoginViewModel
import com.xxjy.umeng.UMengManager
@Route(path = RouteConstants.Personal.A_INPUT_AUTO)
class InputAutoActivity : BindingActivity<ActivityInputAutoBinding, LoginViewModel>() {
    private var time: MyCountDownTime? = null
    protected override fun initView() {
        setTransparentStatusBar(mBinding.toolbar)
        mBinding.inputAutoCodeText.setTextColor(resources.getColor(R.color.colorAccent))
        mBinding.inputAutoCodeText.setTextSize(getResources().getDimensionPixelOffset(R.dimen.sp_20))
        mBinding.inputAutoCodeText.setCursorDrawable(R.drawable.cursor_drawable)
        time = MyCountDownTime.getInstence(60 * 1000, 1000)
        mBinding.sendSmsPhoneNumber.setText(TAG_LOGIN_PHONE_NUMBER)
        time?.start()
    }

     override fun initListener() {
        time?.setOnTimeCountDownListener(object : MyCountDownTime.OnTimeCountDownListener {
            override fun onTick(millisUntilFinished: Long) {
                mBinding.autoCodeShowState.visibility = View.GONE
                mBinding.inputAutoCodeGet.text= "${millisUntilFinished/1000}s"
            }

            override fun onFinish() {
                mBinding.inputAutoCodeGet.text = "重新发送"
                mBinding.autoCodeShowState.visibility = View.VISIBLE
            }
        })
         mBinding.inputAutoCodeText.setOnCompleteListener(object :VerificationCodeInput.Listener{
             override fun onComplete(content: String?) {
                 bindPhone(content!!)
             }

         })
         mBinding.close.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.inputAutoCodeGet.setOnClickListener { view: View -> onViewClicked(view) }
         ClickUtils.applyGlobalDebouncing(mBinding.inputAutoCodeGet,this::onViewClicked)
    }

    @SuppressLint("MissingPermission")
     override fun onViewClicked(view: View?) {
        when (view?.id) {
            R.id.close -> finish()
            R.id.input_auto_code_get -> if (time?.isFinished == true) {
                if (!NetworkUtils.isConnected()) {
                    showToastError("请检查网络配置")
                    return
                }
                autoCode
            }
            else -> {
            }
        }
    }

     override fun dataObservable() {
        mViewModel.mBindPhoneLiveData.observe(this) { data ->
            if (!TextUtils.isEmpty(data)) {
                time?.cancel()
                if (!TextUtils.isEmpty(TAG_LOGIN_WXOPENID)) {
                    UserConstants.openId= TAG_LOGIN_WXOPENID.toString()
                }
                UserConstants.token=data
                UserConstants.login_status=true
                UMengManager.onProfileSignIn("userID")
                //        Tool.postJPushdata();
                if (TextUtils.isEmpty(INVITE_CODE)) {
                    ARouterManager.clearTaskNavigation(RouteConstants.Main.A_MAIN).navigation()
                } else {
                    mViewModel.getSpecOil(INVITE_CODE!!)
                }
            } else {
                mBinding.inputAutoCodeText.clearAllText()
                mBinding.inputAutoCodeText.isEnabled = true
                showToastWarning("绑定失败，请再次尝试")
            }
        }
        mViewModel.mCodeLiveData.observe(this) { data ->
            LogUtils.e("sendCode")
            if (data) {
                showToastSuccess("发送成功")
            } else {
                showToastWarning("发送失败")
            }
        }
        mViewModel.specStationLiveData.observe(this) { data ->
            ActivityUtils.finishActivity(WeChatBindingPhoneActivity::class.java)
            if (!TextUtils.isEmpty(data.data)) {
                ARouterManager.clearTaskNavigation(RouteConstants.Main.A_MAIN).withInt(
                    RouteConstants.ParameterKey.JUMP_STATE,0).navigation()
                BusUtils.postSticky(EventConstants.EVENT_JUMP_HUNTER_CODE, data.data)
            }
            ActivityUtils.finishActivity(InputAutoActivity::class.java)
        }
    }

    private fun getSpanString(time: Long): SpannableStringBuilder {
        return SpanUtils()
            .append("重新发送(" + time + "s)").setForegroundColor(Color.parseColor("#F80235"))
            .create()
    }

    private val autoCode: Unit
        private get() {
            mViewModel.sendCode("10", TAG_LOGIN_PHONE_NUMBER!!)
        }

    private fun bindPhone(autoCode: String) {
        mViewModel.appBindPhone(
            TAG_LOGIN_PHONE_NUMBER!!,
            autoCode,
            TAG_LOGIN_WXOPENID!!,
            TAG_LOGIN_UNIONID!!,
            INVITE_CODE!!,
            JPushInterface.getRegistrationID(this),
            LoginActivity.isInvite
        )
    }

     override fun onDestroy() {
        if (time != null) {
            time?.cancel()
            time = null
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        var TAG_LOGIN_WXOPENID //微信openid
                : String? = null
        var TAG_LOGIN_UNIONID //微信unionId
                : String? = null
        var TAG_LOGIN_PHONE_NUMBER //手机号
                : String? = null
        var INVITE_CODE //邀请人
                : String? = null

        /**
         * 打开输入验证码界面的方法
         *
         * @param activity
         */
        fun openInputAutoCodeAct(activity: Activity) {
            val intent = Intent(activity, InputAutoActivity::class.java)
            activity.startActivity(intent)
        }
    }
}