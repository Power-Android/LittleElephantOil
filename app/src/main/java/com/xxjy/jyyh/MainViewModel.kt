package com.xxjy.jyyh

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xxjy.common.base.BaseViewModel
import kotlinx.coroutines.launch

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
class MainViewModel(application: Application) : BaseViewModel<MainRepository>(application) {
    private val osLiveData = MutableLiveData<Boolean>()

    fun osOver(): LiveData<Boolean> {
        mRespository.osOver(osLiveData)
        return osLiveData
    }

//    val osOverAll: LiveData<Boolean>
//        get() {
//            mRepository.getOsOverAll(osLiveData)
//            return osLiveData
//        }

//    fun newUserStatus(): LiveData<HomeNewUserBean> {
//        val newUserLiveData: MutableLiveData<HomeNewUserBean> = MutableLiveData<HomeNewUserBean>()
//        mRepository.newUserStatus(newUserLiveData)
//        return newUserLiveData
//    }

//    val isNewUser: LiveData<Boolean>
//        get() {
//            val isNewLiveData = MutableLiveData<Boolean>()
//            mRepository.getIsNewUser(isNewLiveData)
//            return isNewLiveData
//        }


}