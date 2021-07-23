package com.xxjy.personal.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.entity.BannerBean
import com.xxjy.personal.entity.MonthCardBean
import com.xxjy.personal.entity.UserBean
import com.xxjy.personal.entity.VipInfoEntity
import com.xxjy.personal.repository.MineRepository

/**
 * @author power
 * @date 1/21/21 12:00 PM
 * @project ElephantOil
 * @description:
 */
class MineViewModel(application: Application) : BaseViewModel<MineRepository>(application) {
    var os1LiveData = MutableLiveData<Boolean>()
    var bannersLiveData: MutableLiveData<List<BannerBean>> = MutableLiveData<List<BannerBean>>()
    var userLiveData: MutableLiveData<UserBean> = MutableLiveData<UserBean>()
    var monthEquityInfoLiveData: MutableLiveData<MonthCardBean> = MutableLiveData<MonthCardBean>()
    var vipInfoLiveData: MutableLiveData<VipInfoEntity> = MutableLiveData<VipInfoEntity>()
    fun queryUserInfo() {
        mRepository.queryUserInfo(userLiveData)
    }

    fun getBannerOfPosition() {
        mRepository.getBannerOfPosition(bannersLiveData)
    }

    fun getOsBalance():MutableLiveData<Boolean> {
        mRepository.getOsBalance(os1LiveData)
        return os1LiveData;
    }

    fun getMonthEquityInfo() {
        mRepository.getMonthEquityInfo(monthEquityInfoLiveData)
    }

    fun getVipInfo() {
        mRepository.getVipInfo(vipInfoLiveData)
    }
}