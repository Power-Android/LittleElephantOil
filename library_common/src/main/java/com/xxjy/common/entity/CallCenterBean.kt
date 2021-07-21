package com.xxjy.common.entity

data class CallCenterBean (
    //
    //    {
    //        "callOnline":"https://dev.qqgyhk.com:8889/adc29a8d6dbc9754?phone=15201061129",
    //            "callPhone":"4008671777",
    //            "callNotice":"工作日9:00-18:00"
    //    }
    var callOnline: String? = null,
    var callPhone: String? = null,
    var callNotice: String? = null
)