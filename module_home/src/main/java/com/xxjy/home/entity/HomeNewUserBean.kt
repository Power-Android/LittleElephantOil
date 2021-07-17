package com.xxjy.home.entity

/**
 * @author power
 * @date 2021/7/16 1:55 下午
 * @project LittleElephantOil_Kotlin
 * @description:
 */
data class HomeNewUserBean(
    var activityId: Long = 0,
    var amount: String,
    var newUrl: String,
    var linkUrl: String,
    var h5Link: String,
    var status: Int = 0
)