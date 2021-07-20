package com.xxjy.personal

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.*
import com.chuanglan.shanyan_sdk.OneKeyLoginManager
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xxjy.common.base.BindingActivity
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.router.ARouterManager
import com.xxjy.common.router.RouteConstants
import com.xxjy.personal.databinding.ActivityLoginBinding
import com.xxjy.personal.viewmodel.LoginViewModel
import com.xxjy.shanyan.SYConfigUtils
import com.xxjy.shanyan.ShanYanManager
import com.xxjy.umeng.UMengLoginWx
import com.xxjy.umeng.UMengManager
@Route(path = RouteConstants.Personal.A_LOGIN)
class LoginActivity : BindingActivity<ActivityLoginBinding, LoginViewModel>() {
    private var isOpenAuth = false //是否已经调起了登录
    private var isDown = false
    private var isInputHunterCode = false //食肉输入猎人码
     override fun initView() {
//        StatusBarUtil.setHeightAndPadding(this, mBinding.toolbar)
        BarUtils.addMarginTopEqualStatusBarHeight( mBinding.toolbar)
        isInvite = getIntent().getBooleanExtra("invite", false)
        tryOpenLoginActivity()
    }

     override fun initListener() {}
     override fun onViewClicked(view: View?) {}
     override fun dataObservable() {
        mViewModel.mVerifyLoginLiveData.observe(this) { s ->
            if (isInputHunterCode) {
                UserConstants.token = s
                UserConstants.login_status = true
                UMengManager.onProfileSignIn("userID")
                ShanYanManager.finishAuthActivity()
                mViewModel.getSpecOil(SYConfigUtils.inviteCode)
            } else {
                mViewModel.setLoginSuccess(s, "")
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
                    UserConstants.openId = openId
                }
                UserConstants.token = token
                UserConstants.login_status = true
                UMengManager.onProfileSignIn("userID")
                //                JPushManager.postJPushdata();
                SYConfigUtils.inviteCode = ""
                if (loginState == Constants.LOGIN_FINISH) {
                    OneKeyLoginManager.getInstance().finishAuthActivity()
                    finish()
                    return@observe
                }
                ARouterManager.clearTaskNavigation(RouteConstants.Main.A_MAIN).navigation()
            } else if (!TextUtils.isEmpty(openId) && !TextUtils.isEmpty(unionId)) {
                showToast("关联微信成功,请您绑定手机号")
                InputAutoActivity.TAG_LOGIN_WXOPENID = openId
                InputAutoActivity.TAG_LOGIN_UNIONID = unionId
                WeChatBindingPhoneActivity.openBindingWxAct(this@LoginActivity)
            } else {
                showToastWarning("登录失败,请使用其他登录方式")
            }
        }
        mViewModel.specStationLiveData.observe(this) { data ->
            if (!TextUtils.isEmpty(data.data)) {
                ARouterManager.clearTaskNavigation(RouteConstants.Main.A_MAIN).withInt(RouteConstants.ParameterKey.JUMP_STATE,0).navigation()
                BusUtils.postSticky(EventConstants.EVENT_JUMP_HUNTER_CODE, data.data)
            }
            ActivityUtils.finishActivity(LoginActivity::class.java)
            ActivityUtils.finishActivity(MobileLoginActivity::class.java)
        }
    }

    @SuppressLint("MissingPermission")
    private fun tryOpenLoginActivity() {
        if (ShanYanManager.isShanYanSupport && NetworkUtils.isConnected()) {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(
                SYConfigUtils.getCJSConfig(this,Constants.USER_XIE_YI ,Constants.YINSI_ZHENG_CE,
                    { context, view -> toLoginForOtherActivity() },
                    { context, view ->
                        if (view.id == R.id.login_for_wx) {
                            loginForWx()
                        }
                    }) { context, view ->
                    val iv = view.findViewById<ImageView>(R.id.iv1)
                    val iv2 = view.findViewById<View>(R.id.iv2)
                    val et: EditText = view.findViewById<EditText>(R.id.invitation_et)
                    view.findViewById<View>(R.id.parent_layout).setOnClickListener { v: View? ->
                        if (isDown) {
                            iv.animate().setDuration(200).rotation(90f).start()
                            et.visibility = View.VISIBLE
                            iv2.visibility = View.VISIBLE
                        } else {
                            iv.animate().setDuration(200).rotation(0f).start()
                            et.visibility = View.GONE
                            iv2.visibility = View.GONE
                        }
                        isDown = !isDown
                    }
                }, null
            )
            openLoginActivity()
        } else {
            toLoginForOtherActivity()
            finish()
        }
    }

    private fun openLoginActivity() {
        //拉取授权页方法
        OneKeyLoginManager.getInstance().openLoginAuth(false,
            { code, result ->
                if (1000 == code) {
                    isOpenAuth = true
                    //                    Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shanyan_dmeo_fade_out_anim);
                    //                    defultRl.startAnimation(animation);
                    //                    defultRl.setVisibility(View.GONE);
                    //拉起授权页成功
                    LogUtils.e("拉起授权页成功： _code==$code   _result==$result")
                } else {
                    //拉起授权页失败
                    LogUtils.e("拉起授权页失败： _code==$code   _result==$result")
                    toLoginForOtherActivity()
                }
            }) { code, result ->
            if (1011 == code) {
                //                    Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shanyan_demo_fade_in_anim);
                //                    defultRl.startAnimation(animation);
                //                    defultRl.setVisibility(View.VISIBLE);
                isOpenAuth = false
                finish()
                //                    LogUtils.e("用户点击授权页返回： _code==" + code + "   _result==" + result);
                //                    return;
            } else if (1000 == code) {
                LogUtils.e("用户点击登录获取token成功： _code==$code   _result==$result")
                //OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                //AbScreenUtils.showToast(getApplicationContext(), "用户点击登录获取token成功");
                try {
                    val shanYanResultBean: SYConfigUtils.ShanYanResultBean =
                        GsonUtils.fromJson(result, SYConfigUtils.ShanYanResultBean::class.java)
                    verifyNormal(shanYanResultBean.token)
                } catch (e: Exception) {
                    showToast("登录失败,请重试或者选择其他登录方式")
                }
            } else {
                //                    LogUtils.e("用户点击登录获取token失败： _code==" + code + "   _result==" + result);
                showToast("登录失败,请重试或者选择其他登录方式")
            }
            isOpenAuth = false
            val startTime = System.currentTimeMillis()
            //                ShanYanManager.finishAuthActivity();
            //                startResultActivity(code, result, startTime);
        }
    }

    private fun verifyNormal(token: String) {
        isInputHunterCode = false
        if (!TextUtils.isEmpty(SYConfigUtils.inviteCode)) {
            isInputHunterCode =
                if (SYConfigUtils.inviteCode.length === 4 || SYConfigUtils.inviteCode.length === 11) {
                    true
                } else {
                    showToastWarning("请输入正确邀请人")
                    OneKeyLoginManager.getInstance().setLoadingVisibility(false)
                    return
                }
        }
        mViewModel.verifyLogin(
            token, JPushInterface.getRegistrationID(this),
            SYConfigUtils.inviteCode, isInvite
        )
    }

    private fun toLoginForOtherActivity() {
        startActivity(Intent(this, MobileLoginActivity::class.java))
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
        mViewModel.openId2Login(openId, accessToken, isInvite)
    }

     override fun onPause() {
        overridePendingTransition(R.anim.bottom_dialog_enter, R.anim.bottom_dialog_exit)
        super.onPause()
    }

    @SuppressLint("MissingPermission")
    override fun finish() {
        if (!ShanYanManager.isShanYanSupport || !NetworkUtils.isConnected()) {
            OneKeyLoginManager.getInstance().finishAuthActivity()
        }
        super.finish()
    }

    companion object {
        var loginState = -1
        var isInvite = false
    }
}