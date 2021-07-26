package com.xxjy.home.entity

/**
 * @author power
 * @date 12/3/20 9:30 PM
 * @project RunElephant
 * @description:
 */
data class LocationEntity (
    var lat: Double= 0.0,//纬度
    var lng: Double = 0.0,//经度
    var city: String? = null,//市
    var district: String? = null,//区
    var address: String? = null,//地址
    var cityCode: String? = null,//城市code
    var adCode: String? = null,//区code
    var isSuccess: Boolean = false,
)