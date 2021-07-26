package com.xxjy.common.entity

/**
 * @author power
 * @date 1/26/21 3:51 PM
 * @project ElephantOil
 * @description:
 */
data class OilEntity(
    var hasStore: Int = 0,//查询附近(默认70km)是否有车服门店 1 存在;0 不存在
    var nearest: Int = 1,//查询最近的是油站还是门店 1 展示油站;0 展示门店
    var stations: List<StationsBean>,
    var storeRecordVo: CarServeStoreDetailsBean
) {
    data class StationsBean(
        var cityName: String,
        var countyName: String,
        var distance: Double,
        var gasAddress: String,
        var gasId: String,
        var gasName: String,
        var gasType: Int,
        var gasTypeImg: String,
        var gasLogoBig: String,
        var gasLogoSmall: String,
        var gasTypeName: String,
        var isInvoice: Int = 0,
        var isIsSign: Boolean,
        var oilName: String,
        var oilNo: String,
        var polyOil: Int,
        var priceGun: String,
        var priceOfficial: String,
        var priceYfq: String,
        var provinceName: String,
        var saveAmount: Double,
        var stationLatitude: Double,
        var stationLongitude: Double,
        var phoneTimeMap: PhoneTimeMapBean,
        var czbLabels: List<CzbLabelsBean>,
        var gunNos: List<GunNosBean>,
        var oilPriceList: List<OilPriceListBean>,
        var topImgList: List<String>
    ) {
        data class PhoneTimeMapBean(
            var hours: String,
            var phone: String
        )

        data class CzbLabelsBean(
            var tagDescription: String,
            var tagImageName: String,
            var tagIndexDescription: String,
            var tagName: String,
            var tagType: String
        )

        data class GunNosBean(
            var gunNo: Int
        )

        data class OilPriceListBean(
            var priceYfq: String,
            var oilType: Int,
            var oilName: String,
            var priceOfficial: String,
            var oilNo: Int,
            var priceGun: String,
            var gunNos: List<GunNosBean>,
            var isSelected: Boolean = false,
            var activityDetailList: List<String>,
        ) {
            data class GunNosBean(
                var gunNo: Int,
                var isSelected: Boolean = false
            )
        }
    }
}
