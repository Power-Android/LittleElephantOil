package com.xxjy.common.entity

/**
 * @author power
 * @date 2021/7/16 2:19 下午
 * @project LittleElephantOil_Kotlin
 * @description:
 */
data class VersionEntity (
    var lastVersion: String,
    var forceUpdate: Int = 0,
    var url: String,
    var description: String
)