package com.xxjy.personal.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.DeviceUtils
import com.chuanglan.shanyan_sdk.OneKeyLoginManager
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.http.Response
import com.xxjy.personal.entity.WeChatLoginBean
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import rxhttp.*

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
class LoginRepository : BaseRepository() {
    fun verifyLogin(
        twinklyToken: String?, jpushId: String?, inviteCode: String, isInvite: Boolean,
        verifyLoginLiveData: MutableLiveData<String>
    ) {
        mRxLifeScope.launch({
            verifyLoginLiveData.value =
                RxHttp.postForm(ApiService.VERIFY_LOGIN)
                    .add("twinklyToken", twinklyToken)
                    .add("did", DeviceUtils.getUniqueDeviceId())
                    .add("jpushId", jpushId)
                    .add(
                        "invitePhone",
                        inviteCode,
                        !TextUtils.isEmpty(inviteCode) && inviteCode.length == 11
                    )
                    .add(
                        "inviteCode",
                        inviteCode,
                        !TextUtils.isEmpty(inviteCode) && inviteCode.length == 4
                    )
                    .add("invite8", 1, isInvite)
                    .toClass<String>()
                    .await()
        }, {
            OneKeyLoginManager.getInstance().setLoadingVisibility(false)
        })

    }

    fun sendCode(
        scene: String?,
        phoneNumber: String?,
        codeLiveData: MutableLiveData<Boolean>
    ): LiveData<Boolean> {
        mRxLifeScope.launch({
            RxHttp.postForm(ApiService.GET_CODE)
                .add("scene", scene)
                .add("mobile", phoneNumber)
                .add(
                    "openId",
                    if (TextUtils.isEmpty(UserConstants.openId)) "" else UserConstants.openId
                )
                .toClass<String>()
                .await()
            codeLiveData.value = true
        }, {
            codeLiveData.value = false
        })
        return codeLiveData
    }

    fun loginByCode(
        codeNumber: String?, phoneNumber: String?, wxOpenId: String?,
        wxUnionId: String?, uuid: String?, registrationID: String?,
        invitationCode: String?, isInvite: Boolean, loginLiveData: MutableLiveData<String>
    ) {
        request(
            {
                loginLiveData.value = RxHttp.postForm(ApiService.VERIFY_LOGIN)
                    .add("phone", phoneNumber)
                    .add("validCode", codeNumber)
                    .add("openId", wxOpenId, !TextUtils.isEmpty(wxOpenId))
                    .add("unionId", wxUnionId, !TextUtils.isEmpty(wxUnionId))
                    .add("did", uuid)
                    .add("jpushId", registrationID)
                    .add(
                        "invitePhone",
                        invitationCode,
                        !TextUtils.isEmpty(invitationCode) && invitationCode?.length == 11
                    )
                    .add(
                        "inviteCode",
                        invitationCode,
                        !TextUtils.isEmpty(invitationCode) && invitationCode?.length == 4
                    )
                    .add("invite8", 1, isInvite)
                    .toClass<String>()
                    .await()
            }, true
        )
    }

    fun openId2Login(
        mWechatLoginLiveData: MutableLiveData<WeChatLoginBean>,
        openId: String?,
        accessToken: String?,
        isInvite: Boolean
    ) {
        request({
            mWechatLoginLiveData.value = RxHttp.postForm(ApiService.WECHAT_LOGIN)
                .add("openId", openId)
                .add("did", DeviceUtils.getUniqueDeviceId())
                .add("accessToken", accessToken)
                .add("invite8", 1, isInvite)
                .toClass<WeChatLoginBean>()
                .await()
        }

        )
    }

    fun appBindPhone(
        mBindPhoneLiveData: MutableLiveData<String>,
        phone: String?,
        validCode: String?,
        openId: String?,
        unionId: String?,
        invitationCode: String,
        jpushId: String?,
        isInvite: Boolean
    ) {
        request({
            RxHttp.postForm(ApiService.APP_BIND_PHONE)
                .add("phone", phone)
                .add("validCode", validCode)
                .add(
                    "invitePhone",
                    invitationCode,
                    !TextUtils.isEmpty(invitationCode) && invitationCode.length == 11
                )
                .add(
                    "inviteCode",
                    invitationCode,
                    !TextUtils.isEmpty(invitationCode) && invitationCode.length == 4
                )
                .add("openId", if (TextUtils.isEmpty(openId)) null else openId)
                .add("unionId", if (TextUtils.isEmpty(unionId)) null else unionId)
                .add("did", DeviceUtils.getUniqueDeviceId())
                .add("jpushId", jpushId)
                .add("invite8", 1, isInvite)
                .toClass<String>()
                .await()
        })
    }

    fun getSpecOil(
        inviteCode: String,
        specStationLiveData: MutableLiveData<Response<String>>
    ) {
        request({
            specStationLiveData.value=RxHttp.postForm(ApiService.GET_SPEC_GAS_ID)
                .add(
                    "invitePhone",
                    inviteCode,
                    !TextUtils.isEmpty(inviteCode) && inviteCode.length == 11
                )
                .add(
                    "inviteCode",
                    inviteCode,
                    !TextUtils.isEmpty(inviteCode) && inviteCode.length == 4
                )
                .toClass<Response<String>>()
                .await()

        }, true)
    }
}