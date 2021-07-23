package com.xxjy.personal.repository

import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.ApiService
import com.xxjy.common.constants.BannerPositionConstants
import com.xxjy.common.entity.BannerBean
import com.xxjy.personal.entity.MonthCardBean
import com.xxjy.personal.entity.UserBean
import com.xxjy.personal.entity.VipInfoEntity
import io.reactivex.rxjava3.functions.Consumer
import rxhttp.RxHttp
import rxhttp.toClass

/**
 * @author power
 * @date 1/21/21 12:00 PM
 * @project ElephantOil
 * @description:
 */
class MineRepository : BaseRepository() {
    fun queryUserInfo(userLiveData: MutableLiveData<UserBean>) {
        request({
            userLiveData.value = RxHttp.postForm(ApiService.USER_INFO)
                .toClass<UserBean>()
                .await()
        })
    }

    fun getBannerOfPosition(bannersLiveData: MutableLiveData<List<BannerBean>>) {
        request({
            bannersLiveData.value = RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                .add("position", BannerPositionConstants.MINE_BANNER)
                .toClass<List<BannerBean>>()
                .await()
        })


    }

    fun getMonthEquityInfo(monthEquityInfoLiveData: MutableLiveData<MonthCardBean>) {
        request({
            monthEquityInfoLiveData.value = RxHttp.postForm(ApiService.GET_MONTH_EQUITY_INFO)
                .toClass<MonthCardBean>()
                .await()
        })
    }

    fun getOsBalance(os1LiveData: MutableLiveData<Boolean>) {
        request({
            os1LiveData.value = RxHttp.postForm(ApiService.GET_OS_BALANCE)
                .toClass<Boolean>()
                .await()
        })
    }

    fun getVipInfo(vipInfoLiveData: MutableLiveData<VipInfoEntity>) {
        request({
            RxHttp.postForm(ApiService.GET_MEMBER_CARD)
                .add("type", "3")
                .toClass<VipInfoEntity>()
                .await()

        }

        )
    }
}