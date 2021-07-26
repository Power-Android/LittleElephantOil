package com.xxjy.home.entity

/**
 * @author power
 * @date 1/29/21 3:59 PM
 * @project ElephantOil
 * @description:
 */
data class HomeProductEntity(
    var className: String,
    var id: Int,
    var firmProductsVo: List<FirmProductsVoBean>
) {
    data class FirmProductsVoBean(
        var id: Int,
        var link: String,
        var name: String,
        var productId: Int,
        var productImg: String,
        var productType: Int,
        var redeemPoint: String,
        var redeemPrice: String,
        var salesNum: Int,
        var type: Int
    )
}