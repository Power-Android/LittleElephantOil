package com.xxjy.common.entity

import java.io.Serializable

data class CarServeProductAttributeBean(
    var hasAppointment: Int,
    var expires: Int,
    var hasNowRefund: Int,
    var carTypeName: String
)