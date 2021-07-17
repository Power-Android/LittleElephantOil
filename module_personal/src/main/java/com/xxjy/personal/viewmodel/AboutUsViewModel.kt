package com.xxjy.personal.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.entity.VersionEntity
import com.xxjy.personal.entity.CallCenterBean
import com.xxjy.personal.repository.AboutUsRepository

class AboutUsViewModel(application: Application) : BaseViewModel<AboutUsRepository>(application) {
    var callCenterLiveData = MutableLiveData<CallCenterBean>()
    var checkVersionLiveData = MutableLiveData<VersionEntity>()

    fun callCenter(): LiveData<CallCenterBean> {
        mRepository.getCallCenter(callCenterLiveData)
        return callCenterLiveData
    }

    fun checkVersion(): LiveData<VersionEntity> {
        mRepository.checkVersion(checkVersionLiveData)
        return checkVersionLiveData
    }
}