package com.xxjy.home

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.xxjy.common.base.BaseRepository
import com.xxjy.common.constants.*
import com.xxjy.common.entity.OilEntity
import com.xxjy.common.provide.MContext
import com.xxjy.home.entity.*
import com.xxjy.navigation.MapLocationHelper
import rxhttp.RxHttp
import rxhttp.toCarServeResponse
import rxhttp.toResponse
import java.util.*

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
class HomeRepository : BaseRepository() {
    /**
     * @param locationLiveData 定位获取经纬度
     */
    fun getLocation(locationLiveData: MutableLiveData<LocationEntity>) {
        val locationEntity = LocationEntity()
        val mapLocation: AMapLocation? = MapLocationHelper.aMapLocation
        if (MapLocationHelper.isLocationValid && mapLocation != null) {
            locationEntity.lat = mapLocation.latitude
            locationEntity.lng = mapLocation.longitude
            locationEntity.city = mapLocation.city
            locationEntity.district = mapLocation.district
            locationEntity.address = mapLocation.address
            locationEntity.isSuccess = true
            locationLiveData.value = locationEntity
        } else {
            MapLocationHelper.getInstance()
                ?.getLocation(MContext.context(), object : MapLocationHelper.LocationResult {
                    override fun locationSuccess(location: AMapLocation) {
                        locationEntity.lat = location.latitude
                        locationEntity.lng = location.longitude
                        locationEntity.city = location.city
                        locationEntity.district = location.district
                        locationEntity.address = location.address
                        locationEntity.isSuccess = true
                        locationLiveData.value = locationEntity
                    }

                    override fun locationFiler() {
                        locationEntity.isSuccess = false
                        locationEntity.address = "暂无定位"
                        locationLiveData.value = locationEntity
                    }
                })
        }
    }

    /**
     * @param lat
     * @param lng
     * @param homeOilLiveData 首页油站
     */
    fun getHomeOil(
        lat: Double, lng: Double, gasId: String,
        homeOilLiveData: MutableLiveData<OilEntity>
    ) {
        request({
            homeOilLiveData.value =
                RxHttp.postForm(ApiService.HOME_OIL)
                    .add(Constants.LATITUDE, lat, lat != 0.0)
                    .add(Constants.LONGTIDUE, lng, lng != 0.0)
                    .add(Constants.GAS_STATION_ID, gasId, !TextUtils.isEmpty(gasId))
                    .toResponse<OilEntity>()
                    .await()
        })
    }

    /**
     * @param oftenOilLiveData 常去油站
     */
    fun getOftenOils(oftenOilLiveData: MutableLiveData<List<OfentEntity>>) {
        mRxLifeScope.launch({
            oftenOilLiveData.value =
                RxHttp.postForm(ApiService.OFTEN_OIL)
                    .toResponse<List<OfentEntity>>()
                    .await()
        }, {
            oftenOilLiveData.value = ArrayList<OfentEntity>()
        })
    }

    /**
     * @param refuelOilLiveData 加油任务
     */
    fun getRefuelJob(refuelOilLiveData: MutableLiveData<QueryRefuelJobEntity>) {
        request({
            refuelOilLiveData.value =
                RxHttp.postForm(ApiService.QUERY_REFUEL_JOB)
                    .toResponse<QueryRefuelJobEntity>()
                    .await()
        })
    }

    /**
     * @param productLiveData 首页积分豪礼
     */
    fun getHomeProduct(productLiveData: MutableLiveData<List<HomeProductEntity.FirmProductsVoBean>>) {
        request({
            val result = RxHttp.postForm(ApiService.HOME_PRODUCT)
                .add(Constants.PAGE, "1")
                .add(Constants.PAGE_SIZE, "10")
                .add("mapKey", ProductMapKeyConstants.INDEX)
                .toResponse<List<HomeProductEntity>>()
                .await()
            productLiveData.value = result[0].firmProductsVo
        })
    }

    fun checkDistance(gasId: String, distanceLiveData: MutableLiveData<OilDistanceEntity>) {
        request({
            distanceLiveData.value =
                RxHttp.postForm(ApiService.GET_PAY_DISTANCE)
                    .add("gasId", gasId)
                    .add(Constants.LATITUDE, UserConstants.latitude)
                    .add(Constants.LONGTIDUE, UserConstants.longitude)
                    .toResponse<OilDistanceEntity>()
                    .await()
        })
    }

    fun getStoreList(
        pageNum: Int,
        pageSize: Int,
        storeLiveData: MutableLiveData<List<OilEntity.StationsBean>>
    ) {
        request({
            storeLiveData.value =
                RxHttp.postForm(ApiService.GET_STORE_LIST)
                    .add("pageNum", pageNum)
                    .add("pageSize", pageSize)
                    .add(Constants.LATITUDE, UserConstants.latitude)
                    .add(Constants.LONGTIDUE, UserConstants.longitude)
                    .toResponse<List<OilEntity.StationsBean>>()
                    .await()
        })
    }

    fun receiverJobCoupon(
        id: String,
        couponId: String,
        receiverCouponLiveData: MutableLiveData<String>
    ) {
        request({
            receiverCouponLiveData.value =
                RxHttp.postForm(ApiService.RECEIVE_OIL_JOB_COUPON)
                    .add("id", id)
                    .add("couponId", couponId)
                    .toResponse<String>()
                    .await()
        })
    }

    fun homeCard(
        lat: Double, lng: Double, gasId: String,
        homeOilLiveData: MutableLiveData<OilEntity>
    ) {
        request({
            homeOilLiveData.value =
                RxHttp.postForm(ApiService.HOME_CARD_INFO)
                    .add(Constants.LATITUDE, lat, lat != 0.0)
                    .add(Constants.LONGTIDUE, lng, lng != 0.0)
                    .add("gpsType", 3)
                    .add(Constants.GAS_STATION_ID, gasId, !TextUtils.isEmpty(gasId))
                    .toResponse<OilEntity>()
                    .await()
        })
    }

    fun getMenu(menuLiveData: MutableLiveData<List<HomeMenuEntity>>) {
        request({
            menuLiveData.value =
                RxHttp.postForm(ApiService.HOME_MENU_INFO)
                    .toResponse<List<HomeMenuEntity>>()
                    .await()
        })
    }

    fun getOftenCars(oftenCarsLiveData: MutableLiveData<List<OftenCarsEntity>>) {
        request({
            oftenCarsLiveData.value =
                RxHttp.postJson(CarServeApiService.OFENT_CAR_SERVE)
                    .addHeader("token", UserConstants.token)
                    .add("appId", CarServeApiService.APP_ID)
                    .add("longitude",
                        if (UserConstants.longitude.toDouble() == 0.0)
                            "116.470866"
                        else
                            UserConstants.longitude
                    )
                    .add("latitude",
                        if (UserConstants.latitude.toDouble() == 0.0)
                            "39.911205"
                        else
                            UserConstants.latitude
                    )
                    .toCarServeResponse<List<OftenCarsEntity>>()
                    .await()
        })
    }
}