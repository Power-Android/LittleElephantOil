package com.xxjy.personal.entity

/**
 * @author power
 * @date 2021/6/11 2:47 下午
 * @project ElephantOil
 * @description:
 */
data class VipInfoEntity(
    var adImgUrl: String? = null,
    var amount: String? = null,
    var awardFlag: Boolean? = null,
    var cardNum: Int? = null,
    var userCardId: String? = null,
    var expire: String? = null,
    var id: String? = null,
    var inviteAmount: String? = null,
    var inviteNum: String? = null,
    var monthRemainStr: String? = null,
    var phone: String? = null,
    var refuelRemainCount: String? = null,
    var remainCount: String? = null,
    var terminusCardNum: String? = null,
    var usableCount: String? = null,
    var description: String? = null,
    var saveMoney: String? = null,
    var firmVipCardEquityVoList: List<FirmVipCardEquityVoListBean>? = null

)