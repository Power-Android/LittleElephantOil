package com.xxjy.common.repos

import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.entity.CallCenterBean
import com.xxjy.common.entity.VersionEntity
import rxhttp.RxHttp
import rxhttp.toClass

class AboutUsRepository : BaseRepository() {
    fun getCallCenter(callCenterLiveData: MutableLiveData<CallCenterBean>) {
        request({
            callCenterLiveData.value= RxHttp.postForm(ApiService.CALL_CENTER)
                .toClass<CallCenterBean>()
                .await()

        }

        )
    }

    fun checkVersion(checkVersionLiveData: MutableLiveData<VersionEntity>) {
        request({
            checkVersionLiveData.value = RxHttp.postForm(ApiService.CHECK_VERSION)
                .toClass<VersionEntity>()
                .await()
        }

        )
    }
}