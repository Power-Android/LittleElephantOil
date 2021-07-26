package com.xxjy.common.entity

import com.xxjy.common.entity.OilEntity.StationsBean.OilPriceListBean

/**
 * @author power
 * @date 2/7/21 7:35 PM
 * @project ElephantOil
 * @description:
 */
data class OilTypeEntity (

    var oilTypeName: String,
    var isSelect: Boolean = false,
    var oilPriceList: List<OilPriceListBean>

//    constructor(oilTypeName: String) {
//        this.oilTypeName = oilTypeName
//    }

//    constructor(oilTypeName: String, isSelect: Boolean) {
//        this.oilTypeName = oilTypeName
//        this.isSelect = isSelect
//    }
//
//    constructor(oilTypeName: String, oilPriceList: List<OilPriceListBean>?) {
//        this.oilTypeName = oilTypeName
//        this.oilPriceList = oilPriceList
//    }
){
    constructor(
        oilTypeName: String,
        oilPriceList: List<OilPriceListBean>) : this(
        oilTypeName,
        isSelect = false,
        oilPriceList)
}