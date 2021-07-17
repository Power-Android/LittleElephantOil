package com.xxjy.common.entity

/**
 * @author power
 * @date 2021/7/17 3:48 下午
 * @project LittleElephantOil_Kotlin
 * @description:
 */
data class HomeNewUserBean(

    var activityId: Long = 0L,
    var amount: String,
    var newUrl: String,
    var linkUrl: String,
    var h5Link: String,
    var status: Int = 0
)