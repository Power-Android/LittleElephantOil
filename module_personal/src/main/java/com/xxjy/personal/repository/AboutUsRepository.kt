package com.xxjy.personal.repository

import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.entity.VersionEntity
import com.xxjy.personal.entity.CallCenterBean
import rxhttp.RxHttp
import rxhttp.toResponse

class AboutUsRepository : BaseRepository() {
    fun getCallCenter(callCenterLiveData: MutableLiveData<CallCenterBean>) {
        request({
            callCenterLiveData.value =
                RxHttp.postForm(ApiService.CALL_CENTER)
                    .toResponse<CallCenterBean>()
                    .await()
        })
    }

    fun checkVersion(checkVersionLiveData: MutableLiveData<VersionEntity>) {
        request({
            checkVersionLiveData.value =
                RxHttp.postForm(ApiService.CHECK_VERSION)
                    .toResponse<VersionEntity>()
                    .await()
        })
    }
}