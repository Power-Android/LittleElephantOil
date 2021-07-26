package com.xxjy.home.entity

/**
 * @author power
 * @date 2021/6/18 1:54 下午
 * @project ElephantOil
 * @description:
 */
data class OftenCarsEntity(
    var cardStoreInfoVo: CardStoreInfoVoBean,
    var products: List<*>
) {
    data class CardStoreInfoVoBean(
        var id: Int,
        var storeNo: String,
        var thirdNo: String,
        var supplierStoreNo: String,
        var merchantId: String,
        var merchantName: String,
        var status: Int,
        var contact: String,
        var mobile: String,
        var phone: String,
        var type: Int,
        var province: String,
        var provinceCode: String,
        var city: String,
        var cityCode: String,
        var area: String,
        var areaCode: String,
        var address: String,
        var storeName: String,
        var storePicture: String,
        var channel: Int,
        var serviceType: Int,
        var supplierName: String,
        var longitude: Double,
        var latitude: Double,
        var openStart: String,
        var endStart: String,
        var createPerson: String,
        var createTime: String,
        var updatePerson: String,
        var updateTime: String,
        var openHoliday: String,
        var endHoliday: String,
        var holidayReason: String,
        var deletedTime: String,
        var isOnline: Int,
        var distance: Double,
        var categoryNameList: Any
    )
}
