package com.xxjy.common.entity

data class BannerBean (
    /**
     * id : 22
     * imgUrl : http://okche.oss-cn-shanghai.aliyuncs.com/tyb/20180513/134426Rcr.png
     * title : 新手优惠券
     * link : http://twx.yfq360.com/found/digitalSpecial
     */
    var id :Int= 0,
    var dateId: String,
    var imgUrl: String,
    var title: String,
    var link: String,
    var startTime: String,
    var endTime: String,
)