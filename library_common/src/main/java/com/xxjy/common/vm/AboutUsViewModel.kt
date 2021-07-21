package com.xxjy.common.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.entity.CallCenterBean
import com.xxjy.common.entity.VersionEntity
import com.xxjy.common.repos.AboutUsRepository

class AboutUsViewModel(application: Application) : BaseViewModel<AboutUsRepository>(application) {
    var callCenterLiveData: MutableLiveData<CallCenterBean> = MutableLiveData<CallCenterBean>()
    var checkVersionLiveData: MutableLiveData<VersionEntity> = MutableLiveData<VersionEntity>()
    fun getCallCenter() {
            mRepository.getCallCenter(callCenterLiveData)
        }

    fun checkVersion() {
        mRepository.checkVersion(checkVersionLiveData)
    }
}