package com.xxjy.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.xxjy.common.base.BaseViewModel
import com.xxjy.common.entity.OilEntity
import com.xxjy.home.entity.*

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
class HomeViewModel(application: Application) : BaseViewModel<HomeRepository>(application) {
    var locationLiveData = MutableLiveData<LocationEntity>()
    var homeOilLiveData = MutableLiveData<OilEntity>()
    var oftenOilLiveData = MutableLiveData<List<OfentEntity>>()
    var refuelOilLiveData = MutableLiveData<QueryRefuelJobEntity>()
    var receiverCouponLiveData = MutableLiveData<String>()
    var productLiveData = MutableLiveData<List<HomeProductEntity.FirmProductsVoBean>>()
    var distanceLiveData = MutableLiveData<OilDistanceEntity>()
    var storeLiveData = MutableLiveData<List<OilEntity.StationsBean>>()
    var menuLiveData = MutableLiveData<List<HomeMenuEntity>>()
    var oftenCarsLiveData = MutableLiveData<List<OftenCarsEntity>>()

    fun location() {
        mRepository.getLocation(locationLiveData)
    }

    fun oftenOils() {
        mRepository.getOftenOils(oftenOilLiveData)
    }

    fun refuelJob() {
        mRepository.getRefuelJob(refuelOilLiveData)
    }

    fun homeProduct() {
        mRepository.getHomeProduct(productLiveData)
    }

    fun checkDistance(gasId: String) {
        mRepository.checkDistance(gasId, distanceLiveData)
    }

    fun getStoreList(pageNum: Int, pageSize: Int) {
        mRepository.getStoreList(pageNum, pageSize, storeLiveData)
    }

    fun receiverJobCoupon(id: String, couponId: String) {
        mRepository.receiverJobCoupon(id, couponId, receiverCouponLiveData)
    }

    fun homeCard(lat: Double, lng: Double, gasId: String) {
        mRepository.homeCard(lat, lng, gasId, homeOilLiveData)
    }

    fun menu() {
        mRepository.getMenu(menuLiveData)
    }

    fun oftenCars() {
        mRepository.getOftenCars(oftenCarsLiveData)
    }
}