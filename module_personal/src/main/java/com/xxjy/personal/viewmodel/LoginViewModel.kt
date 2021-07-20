package com.xxjy.personal.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.http.Response
import com.xxjy.personal.LoginActivity
import com.xxjy.personal.entity.WeChatLoginBean
import com.xxjy.personal.repository.LoginRepository
import com.xxjy.shanyan.ShanYanManager
import com.xxjy.umeng.UMengManager

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
class LoginViewModel(application: Application) : BaseViewModel<LoginRepository>(application) {
    var mVerifyLoginLiveData = MutableLiveData<String>()
    var mCodeLiveData = MutableLiveData<Boolean>()
    var mLoginLiveData = MutableLiveData<String>()
    var mWechatLoginLiveData = MutableLiveData<WeChatLoginBean>()
    var mBindPhoneLiveData = MutableLiveData<String>()
    var specStationLiveData = MutableLiveData<Response<String>>()
    fun verifyLogin(
        twinklyToken: String?,
        jpushId: String?,
        inviteCode: String?,
        isInvite: Boolean
    ) {
        mRepository.verifyLogin(
            twinklyToken,
            jpushId,
            inviteCode!!,
            isInvite,
            mVerifyLoginLiveData
        )
    }

    fun sendCode(scene: String, phoneNumber: String): LiveData<Boolean> {
        return mRepository.sendCode(scene, phoneNumber, mCodeLiveData)
    }

    fun loginByCode(
        codeNumber: String?,
        phoneNumber: String?,
        wxOpenId: String?,
        wxUnionId: String?,
        uuid: String?,
        registrationID: String?,
        invitationCode: String?,
        isInvite: Boolean
    ) {
        mRepository.loginByCode(
            codeNumber, phoneNumber, wxOpenId, wxUnionId, uuid,
            registrationID, invitationCode, isInvite, mLoginLiveData
        )
    }

    fun setLoginSuccess(token: String, mobile: String) {
        UserConstants.token=token
        UserConstants.mobile=mobile
        UserConstants.login_status=true
        UMengManager.onProfileSignIn("userID")
        //        JPushManager.postJPushdata();
        ActivityUtils.finishActivity(LoginActivity::class.java)
        ShanYanManager.finishAuthActivity()
        // TODO: 2021/7/19  
//        ActivityUtils.finishActivity(MobileLoginActivity::class.java)
    }

    fun openId2Login(openId: String?, accessToken: String?, isInvite: Boolean) {
        mRepository.openId2Login(mWechatLoginLiveData, openId, accessToken, isInvite)
    }

    fun appBindPhone(
        phone: String,
        validCode: String,
        openId: String,
        unionId: String,
        invitationCode: String,
        jpushId: String?,
        isInvite: Boolean
    ) {
        mRepository.appBindPhone(
            mBindPhoneLiveData,
            phone,
            validCode,
            openId,
            unionId,
            invitationCode,
            jpushId,
            isInvite
        )
    }

    fun getSpecOil(inviteCode: String) {
        mRepository.getSpecOil(inviteCode, specStationLiveData)
    }
}