package com.xxjy.jyyh

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import androidx.lifecycle.viewModelScope
import com.xxjy.common.base.BaseViewModel
import com.xxjy.home.entity.HomeNewUserBean
import kotlinx.coroutines.launch

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
class MainViewModel(application: Application) : BaseViewModel<MainRepository>(application) {
    private val osLiveData = MutableLiveData<Boolean>()
    private val isNewLiveData = MutableLiveData<Boolean>()
    private val newUserLiveData = MutableLiveData<HomeNewUserBean>()

    fun osOver(): LiveData<Boolean> {
        mRepository.osOver(osLiveData)
        return osLiveData
    }
    fun newUserStatus(): LiveData<HomeNewUserBean> {
        mRepository.newUserStatus(newUserLiveData)
        return newUserLiveData
    }

    fun isNewUser(): LiveData<Boolean> {
        mRepository.isNewUser(isNewLiveData)
        return isNewLiveData
    }
}