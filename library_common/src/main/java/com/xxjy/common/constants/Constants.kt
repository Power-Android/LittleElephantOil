package com.xxjy.common.constants

import com.xxjy.common.constants.ApiService.CONFIG_BASE_URL

/**
 * @author power
 * @date 12/1/20 1:38 PM
 * @project RunElephant
 * @description:
 */
object Constants {
    val USER_XIE_YI: String = CONFIG_BASE_URL + "found/articles/400221"
    val YINSI_ZHENG_CE: String = CONFIG_BASE_URL + "found/articles/400222"
    val INTEGRAL_EXPLANATION_URL: String = CONFIG_BASE_URL + "mall/introduction"

    //签到链接https://tcore.qqgyhk.com/oil/marketList?meet=1,
    // 营销活动聚合页https://tcore.qqgyhk.com/oil/marketList，
    // 购买月卡https://tcore.qqgyhk.com/oil/monthCardBuy，
    // 提前购买https://tcore.qqgyhk.com/oil/monthCard，
    // 新人好礼https://tcore.qqgyhk.com/oil/newUserGift
    val SIGN_IN_URL: String = CONFIG_BASE_URL  + "oil/marketList?meet=1"
    val MARKET_ACTIVITIES_URL: String = CONFIG_BASE_URL  + "oil/marketList"
    val BUY_MONTH_CARD_URL: String = CONFIG_BASE_URL  + "oil/monthCardBuy"
    val BUY_IN_ADVANCE_MONTH_CARD_URL: String = CONFIG_BASE_URL  + "oil/monthCard"
    val NEW_USER_GIFT_URL: String = CONFIG_BASE_URL  + "oil/newUserGift"
    val VIP_URL: String = CONFIG_BASE_URL  + "membership?id=1"

    /**
     * release服务器
     */
    const val URL_IS_DEBUG = true //测试用这个
    //    public static final boolean URL_IS_DEBUG = false;   //正式上线用这个
    /**
     * 配置debug模式
     */
    const val IS_DEBUG = true //测试用这个

    //    public static final boolean IS_DEBUG = false;   //正式上线用这个
    //微信配置
    const val WX_APP_ID = "wx3704434db8357ec1"
    const val WX_APP_SCRIPT =
        "787d5dcefab80f6bca272800e9bad139" //ab730ab00dd73986593da2ce6514ffe8     6b4edd26960e017c050f940210a99723

    //微信支付回调地址
    val HTTP_CALL_BACK_URL =
        if (URL_IS_DEBUG) "https://tcore.qqgyhk.com" else "https://core.qqgyhk.com"

    //fragment
    const val CURRENT_FRAGMENT_KEY = "current_fragment"
    const val TYPE_HOME = 0
    const val TYPE_OIL = 1
    const val TYPE_CAR_SERVE = 2
    const val TYPE_INTEGRAL = 3
    const val TYPE_MINE = 4

    //回退事件
    const val DOUBLE_INTERVAL_TIME: Long = 2000

    //登录处理
    var LOGIN_FINISH = 1 //finish掉

    //猎人码油站id
    var HUNTER_GAS_ID //finish掉
            : String? = null

    //是否点击关闭定位权限授权
    var OPEN_LOCATION_VISIBLE = true //finish掉

    //h5链接
    //入参key
    const val PAGE = "pageNum"
    const val PAGE_SIZE = "pageSize"
    const val LATITUDE = "appLatitude"
    const val LONGTIDUE = "appLongitude"
    const val GAS_STATION_ID = "gasId" //油站id
    const val OIL_NUMBER_ID = "oilNo" //油号
    const val TYPE = "type"
    const val NICK_NAME = "nickName"
    const val NICK_NAME_2 = "nickname"
    const val IMG_URL = "imgUrl"
    const val SEX = "sex"
    const val BIRTHDAY = "birthday"
    const val WX_ACCOUNT = "wxAccount"
    const val FILE = "file"

    //eventBus
    var PV_ID = 0
}