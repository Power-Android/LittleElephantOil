package com.xxjy.common.entity

import com.google.gson.annotations.SerializedName

/**
 * @author power
 * @date 12/9/20 3:31 PM
 * @project RunElephant
 * @description:
 */
data class EventEntity(
    @SerializedName("event")
    var event: String
)
