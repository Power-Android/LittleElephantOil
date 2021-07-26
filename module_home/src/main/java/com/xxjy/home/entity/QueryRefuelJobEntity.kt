package com.xxjy.home.entity

import java.io.Serializable

/**
 * @author power
 * @date 2021/5/12 2:05 下午
 * @project ElephantOil
 * @description:
 */
class QueryRefuelJobEntity(
    var amount: Int,
    var coupons: List<CouponsBean>,
    var progress: Int,
    var totalProgress: Int,
    var usable: Int,
    var id: String,
    var url: String,
    var isButton: Boolean = false
) {
    data class CouponsBean(
        var couponId: Int,
        var info: String,
        var number: Int,
        var status: Int
    )
}