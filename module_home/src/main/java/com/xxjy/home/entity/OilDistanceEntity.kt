package com.xxjy.home.entity

/**
 * @author power
 * @date 1/31/21 5:29 PM
 * @project ElephantOil
 * @description:
 */
data class OilDistanceEntity(
    var distance: String,
    var gasId: String,
    var gasName: String,
    var latitude: String,
    var longitude: String,
    var isHere: Boolean = false,
    var isPay: Boolean = false
)