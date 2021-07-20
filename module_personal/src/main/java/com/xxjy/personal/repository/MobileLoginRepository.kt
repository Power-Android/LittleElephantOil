package com.xxjy.personal.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.constants.UserConstants
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import rxhttp.RxHttp
import rxhttp.toClass

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
class MobileLoginRepository : BaseRepository() {
    fun sendCode(
        scene: String?,
        phoneNumber: String?,
        codeLiveData: MutableLiveData<Boolean?>
    ): LiveData<Boolean?> {
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
            codeLiveData.postValue(false)
        })
        return codeLiveData
    }

    fun loginByCode(
        codeNumber: String?, phoneNumber: String?, wxOpenId: String?,
        wxUnionId: String?, uuid: String?, registrationID: String?,
        invitationCode: String?, loginLiveData: MutableLiveData<String?>
    ) {
        request({
            loginLiveData.value= RxHttp.postForm(ApiService.VERIFY_LOGIN)
                .add("phone", phoneNumber)
                .add("validCode", codeNumber)
                .add("openId", wxOpenId, !TextUtils.isEmpty(wxOpenId))
                .add("unionId", wxUnionId, !TextUtils.isEmpty(wxUnionId))
                .add("did", uuid)
                .add("jpushId", registrationID)
                .add("invitePhone", invitationCode, !TextUtils.isEmpty(invitationCode))
                .toClass<String>()
                .await()
        }, true)
    }
}