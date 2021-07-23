package com.xxjy.personal.entity

data class UserBean (
    //    {"code":1,"data":{"headImg":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202101/26165443fSe.png","balance":0,"phone":"152****1129","couponsSize":0,"integralBalance":0},"serviceTime":1611741507956}
    var headImg: String? = null,
    var balance: String? = null,
    var phone: String? = null,
    var couponsSize: String? = null,
    var integralBalance: String? = null,
    var walletUrl: String? = null,
    var integralBillUrl: String? = null,
    var totalDiscountPre: String? = null,
    var monthCardBuyUrl: String? = null,
    var isHasBuyOldMonthCoupon:Boolean = false,
    var monthCardTotalDiscount: String? = null,
    var monthCardExpireDate: String? = null,
     var eVipOpenFlag:Boolean = false
)
