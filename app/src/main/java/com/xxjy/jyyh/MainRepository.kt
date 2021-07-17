package com.xxjy.jyyh

import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.entity.HomeNewUserBean
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
class MainRepository : BaseRepository() {

    fun osOver(osLiveData: MutableLiveData<Boolean>) {
        request({
            osLiveData.value =
                RxHttp.postForm(ApiService.GET_OS_OVERALL)
                .toResponse<Boolean>()
                .await()
        })
    }

    fun isNewUser(isNewLiveData: MutableLiveData<Boolean>) {
        request({
            isNewLiveData.value =
                RxHttp.postForm(ApiService.IS_NEW_USER)
                .toResponse<Boolean>()
                .await()
        })
    }

    fun newUserStatus(newUserLiveData: MutableLiveData<HomeNewUserBean>) {
        request({
            newUserLiveData.value =
                RxHttp.postForm(ApiService.NEW_USER_STATUS)
                .toResponse<HomeNewUserBean>()
                .await()
        })
    }
}