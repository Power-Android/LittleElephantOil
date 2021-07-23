package com.xxjy.personal

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.*
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.common.weight.MyCountDownTime
import com.xxjy.personal.databinding.ActivityMobileLoginBinding
import com.xxjy.personal.viewmodel.LoginViewModel
import com.xxjy.shanyan.ShanYanManager
import com.xxjy.umeng.UMengLoginWx
import com.xxjy.umeng.UMengManager

class MobileLoginActivity : BindingActivity<ActivityMobileLoginBinding, LoginViewModel>() {
    private var mCountDownTime: MyCountDownTime? = null
    private val mMaxPhoneLength = 13
    private var lineGetFocus = 0
    private var lineUnFocus = 0
    private var mPhoneNumber: String? = null
    private var isDown = false
    private val wxOpenId: String? = null
    private val wxUnionId: String? = null
    private var isInputHunterCode = false //食肉输入猎人码
    protected override fun initView() {
        setTransparentStatusBar(mBinding.topLayout)
        lineUnFocus = Color.parseColor("#B8B8B8")
        lineGetFocus = Color.parseColor("#000000")
        mCountDownTime = MyCountDownTime.getInstence(60 * 1000, 1000)
        SpanUtils.with(mBinding.tipView)
            .append("登录即接受并同意遵守我们的")
            .append("《服务协议》")
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ARouterManager.navigation(RouteConstants.Web.A_WEB).withString(RouteConstants.ParameterKey.URL,Constants.USER_XIE_YI).navigation()
                }

                override fun updateDrawState(ds: TextPaint) {
//                        super.updateDrawState(ds);
                    ds.isUnderlineText = false
                }
            })
            .setForegroundColor(Color.parseColor("#1676FF"))
            .append("和")
            .append("《隐私政策》")
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ARouterManager.navigation(RouteConstants.Web.A_WEB).withString(RouteConstants.ParameterKey.URL,Constants.YINSI_ZHENG_CE).navigation()
                }

                override fun updateDrawState(ds: TextPaint) {
//                        super.updateDrawState(ds);
                    ds.isUnderlineText = false
                }
            })
            .setForegroundColor(Color.parseColor("#1676FF"))
            .append("以及个人敏感信息政策")
            .create()
    }

    protected override fun initListener() {
        mBinding.loginGetCode.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.loginV3Login.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.userInviteNumberLayout.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.loginForWx.setOnClickListener { view: View -> onViewClicked(view) }
        mBinding.close.setOnClickListener { view: View -> onViewClicked(view) }
        ClickUtils.applyGlobalDebouncing(arrayOf(mBinding.loginGetCode, mBinding.loginV3Login),this::onViewClicked)
        mCountDownTime?.setOnTimeCountDownListener(object : MyCountDownTime.OnTimeCountDownListener {
           override fun onTick(millisUntilFinished: Long) {
               mBinding.loginGetCode.text = "已发送 ( ${millisUntilFinished / 1000} S)"
            }

            override fun onFinish() {
                mBinding.loginGetCode.isEnabled = true
                mBinding.loginGetCode.text = "重新获取"
            }
        })
        mBinding.userPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                mBinding.loginGetCode.isEnabled = s.toString().trim { it <= ' ' }.length >= mMaxPhoneLength
                //                if (s.toString().length() > 0) {
//                    mBinding.registerUserClearPhone.setVisibility(View.VISIBLE);
//                } else {
//                    mBinding.registerUserClearPhone.setVisibility(View.GONE);
//                }
                refreshLoginState()
            }
        })
        mBinding.userPhoneCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                refreshLoginState()
            }
        })
        mBinding.userPhoneNumber.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (mBinding == null || mBinding.userPhoneNumberLine == null) {
                    return
                }
                if (hasFocus || !TextUtils.isEmpty(mBinding.userPhoneNumber.getTextWithoutSpace())) {
                    mBinding.userPhoneNumberLine.setBackgroundColor(lineGetFocus)
                } else {
                    mBinding.userPhoneNumberLine.setBackgroundColor(lineUnFocus)
                }
            }
        }
        mBinding.userPhoneCode.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (mBinding == null || mBinding.userPhoneCodeLine == null) {
                    return
                }
                if (hasFocus || !TextUtils.isEmpty(mBinding.userPhoneCode.getText())) {
                    mBinding.userPhoneCodeLine.setBackgroundColor(lineGetFocus)
                } else {
                    mBinding.userPhoneCodeLine.setBackgroundColor(lineUnFocus)
                }
            }
        }
    }

    private fun refreshLoginState() {
        val phoneNumber: String = mBinding.userPhoneNumber.getTextWithoutSpace()
        val codeNumber: String = mBinding.userPhoneCode.getText().toString()
        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(codeNumber)
            && phoneNumber.length >= 11 && codeNumber.length > 5
        ) {
            mBinding.loginV3Login.setEnabled(true)
        } else {
            mBinding.loginV3Login.setEnabled(false)
        }
    }

    protected override fun onViewClicked(view: View?) {
        when (view?.id) {
            R.id.login_get_code -> {
                if (mCountDownTime?.isFinished!=true) {
                    return
                }
                mPhoneNumber = mBinding.userPhoneNumber.getTextWithoutSpace()
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning("请填写手机号")
                    return
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning("请输入正确手机号")
                    return
                }
                autoCode
            }
            R.id.close -> onBackPressed()
            R.id.login_v3_login -> {
                mPhoneNumber = mBinding.userPhoneNumber.getTextWithoutSpace()
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning("请填写手机号")
                    return
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning("请输入正确手机号")
                    return
                }
                val codeNumber: String = mBinding.userPhoneCode.getText().toString()
                if (TextUtils.isEmpty(codeNumber)) {
                    showToastWarning("请输入验证码")
                    return
                }
                val inviteNumber: String = mBinding.invitationEt.getText().toString().trim()
                isInputHunterCode = false
                if (!TextUtils.isEmpty(inviteNumber)) {
                    isInputHunterCode = if (inviteNumber.length == 4 || inviteNumber.length == 11) {
                        true
                    } else {
                        showToastWarning("请输入正确邀请人")
                        return
                    }
                }
                loginByCode(codeNumber)
            }
            R.id.login_for_wx -> loginForWx()
            R.id.user_invite_number_layout -> isDown = if (isDown) {
                mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(90f).start()
                mBinding.invitationLl.visibility = View.GONE
                false
            } else {
                mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(0f).start()
                mBinding.invitationLl.visibility = View.VISIBLE
                true
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mCountDownTime?.stopCountDown()
        mCountDownTime?.cancel()
        mCountDownTime = null
    }

    private val autoCode: Unit
        private get() {
            mCountDownTime?.start()
            mBinding.loginGetCode.isEnabled = false
            mViewModel.sendCode("2", mPhoneNumber!!)
                .observe(this) { b ->
                    if (b) {
                        showToastSuccess("发送成功")
                    } else {
                        showToastError("发送失败，请重试")
                        mCountDownTime?.onFinish()
                    }
                }
        }

    private fun loginByCode(codeNumber: String) {
        mViewModel.loginByCode(
            codeNumber, mPhoneNumber, wxOpenId, wxUnionId, DeviceUtils.getUniqueDeviceId(),
            JPushInterface.getRegistrationID(this),
            mBinding.invitationEt.text.toString(), LoginActivity.isInvite
        )
    }

    private fun loginForWx() {
        UMengLoginWx.loginFormWx(this, object : UMengLoginWx.UMAuthAdapter() {
            override fun onComplete(share_media: SHARE_MEDIA?, i: Int, map: Map<String?, String?>?) {
                if (map != null && map.containsKey("openid") && map.containsKey("accessToken")) {
                    val openId = map["openid"]
                    val accessToken = map["accessToken"]
                    openId2Login(openId, accessToken)
                }
            }
        })
    }

    private fun openId2Login(openId: String?, accessToken: String?) {
        mViewModel.openId2Login(openId, accessToken, LoginActivity.isInvite)
    }

    protected override fun dataObservable() {
        mViewModel.loadingView().observe(this) { aBoolean ->
            if (aBoolean) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }
        mViewModel.mLoginLiveData.observe(this) { s ->
            mCountDownTime?.stopCountDown()
            mCountDownTime?.cancel()
            mCountDownTime = null
            if (isInputHunterCode) {
                UserConstants.token=s
                UserConstants.login_status=true
                UMengManager.onProfileSignIn("userID")
                ShanYanManager.finishAuthActivity()
                mViewModel.getSpecOil(mBinding.invitationEt.getText().toString().trim())
            } else {
                mViewModel.setLoginSuccess(s, mPhoneNumber!!)
            }
        }
        mViewModel.mWechatLoginLiveData.observe(this) { data ->
            if (data == null) {
                showToastWarning("登录失败,请使用其他登录方式")
                return@observe
            }
            val token: String = data.token
            val openId: String = data.openId
            val unionId: String = data.unionId
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(openId)) {
//                            loginToMain(token, "", openId);
                if (!TextUtils.isEmpty(openId)) {
                    UserConstants.openId=openId
                }
                UserConstants.token=token
                UserConstants.login_status=true
                UMengManager.onProfileSignIn("userID")
                //                JPushManager.postJPushdata();
                if (LoginActivity.loginState == Constants.LOGIN_FINISH) {
                    finish()
                    return@observe
                }
                ARouterManager.navigationClearTask(RouteConstants.Main.A_MAIN).navigation()
            } else if (!TextUtils.isEmpty(openId) && !TextUtils.isEmpty(unionId)) {
                showToast("关联微信成功,请您绑定手机号")
                InputAutoActivity.TAG_LOGIN_WXOPENID = openId
                InputAutoActivity.TAG_LOGIN_UNIONID = unionId
                WeChatBindingPhoneActivity.openBindingWxAct(this@MobileLoginActivity)
            } else {
                showToastWarning("登录失败,请使用其他登录方式")
            }
        }
        mViewModel.specStationLiveData.observe(this) { data ->
            if (!TextUtils.isEmpty(data.data)) {
//                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT, new EventEntity(EventConstants.EVENT_TO_HOME_FRAGMENT));
                ARouterManager.navigationClearTask(RouteConstants.Main.A_MAIN).withInt(RouteConstants.ParameterKey.JUMP_STATE,0).navigation()
                BusUtils.postSticky(EventConstants.EVENT_JUMP_HUNTER_CODE, data.data)
            }
            ActivityUtils.finishActivity(LoginActivity::class.java)
            ActivityUtils.finishActivity(MobileLoginActivity::class.java)
        }
    }
}