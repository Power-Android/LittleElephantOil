package com.xxjy.common.constants

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @author power
 * @date 12/1/20 1:41 PM
 * @project RunElephant
 * @description: 网络请求专用
 */
object ApiService {
    //默认正式服务器url
    const val CONFIG_RELEASE_URL = "https://m.qqgyhk.com/"
    const val RELEASE_URL = CONFIG_RELEASE_URL + "server/"

    //默认测试服务器url
    const val CONFIG_DEBUG_URL = "https://tcore.qqgyhk.com/"

    //    public static final String CONFIG_DEBUG_URL = "https://ccore.qqgyhk.com/";
    const val DEBUG_URL = CONFIG_DEBUG_URL + "server/"

//    @DefaultDomain //默认域名
     var BASE_URL = if (Constants.URL_IS_DEBUG) DEBUG_URL else RELEASE_URL
     var CONFIG_BASE_URL = if (Constants.URL_IS_DEBUG) CONFIG_DEBUG_URL else CONFIG_RELEASE_URL

    //获取验证码
    const val GET_CODE = "api/v1/wx/sendSmsCode"

    //闪验、验证码登录
    const val VERIFY_LOGIN = "api/v1/user/flash/login"

    //移动端微信授权登录
    const val WECHAT_LOGIN = "api/v1/user/openId2Login"

    //app绑定手机号码
    const val APP_BIND_PHONE = "api/v1/user/flash/appBindPhone"

    //获取首页油站
    const val HOME_OIL = "api/gasPublic/getHomeOilStations"

    //加油订单滚动消息
    const val ORDER_NEWS = "api/gasPublic/getOrderNews"

    //获取油号列表
    const val OIL_NUMS = "api/gasPublic/getOilNums"

    //油站筛选
    const val OIL_STATIONS = "api/gasPublic/getOilStations"

    //加油签约油站列表
    const val SIGN_OIL_STATIONS = "api/gasPublic/getSignOilStations"

    //油站列表
    val OIL_AND_SIGN_STATIONS = BASE_URL + "api/gasPublic/getOilAndSignStations"

    //加油首页banner
    const val OIL_STATIONS_BANNERS = "api/gasPublic/getBanners"

    //获取商品分类列表
    const val PRODUCT_CATEGORYS = "api/product/v1/queryProductCategorys"

    //根据商品分类获取商品列表
    const val QUERY_PRODUCTS = "api/product/v1/queryProducts"

    //查询个人账户信息
    const val USER_INFO = "api/user/queryUserInfo"

    //最近常去的油站
    const val OFTEN_OIL = "api/gasPublic/getPreferentialStations"

    //首页累计加油任务
    const val REFUEL_JOB = "api/gasPublic/getRefuelJob"

    //获取油号列表-筛选用
    const val GET_OIL_NUM = "api/gasPublic/getOilNums"

    //商户优惠券
    const val BUSINESS_COUPON = "api/coupon/v1/getBusinessCoupons"
    const val BUSINESS_COUPON_LIST = "api/coupon/v1/getBusinessCouponsList"

    //兑换优惠券
    const val EXCHANGE_COUPON = "api/coupon/v1/exchangeCoupon"

    //平台优惠券
    const val PLATFORM_COUPON = "api/coupon/v1/getPlatformCouponVOs"
    const val PLATFORM_COUPON_LIST = "api/coupon/v1/getMyCouponList"

    //优惠券数量
    const val GET_COUPON_NUM = "api/coupon/v1/getMyCouponNumber"

    //油站默认快捷价格
    const val OIL_PRICE_DEFAULT = "api/gasPublic/buyPriceDefault"

    //油站优惠券互斥
    const val OIL_MULTIPLE_PRICE = "api/tiein/v1/getPayPrices"

    //获取某个位置的banner
    const val GET_BANNER_OF_POSITION = "api/v1/banner/getBannerOfPostion"

    //获取加油订单列表
    const val REFUEL_ORDER_LIST = "api/gasPublic/refuelOrderList"

    //获取加油订单退单列表
    const val REFUND_ORDER_LIST = "api/gasPublic/orderRefundList"

    //获取订单列表
    const val INTEGRAL_ORDER_LIST = "api/product/v1/queryMallOrderList"

    //获取加油订单详情
    const val REFUEL_ORDER_DETAILS = "api/gasPublic/refuelOrderDetails"

    //退款订单详情接口
    const val ORDER_REFUND_DETAILS = "api/gasPublic/orderRefundDetail"

    //取消加油订单
    const val REFUEL_ORDER_CANCEL = "api/gasPublic/cancelOrder"

    //取消订单 积分商城订单
    const val PRODUCT_ORDER_CANCEL = "api/product/v1/cancelOrder"

    //申请退款
    const val REFUEL_APPLY_REFUND = "api/gasPublic/refuelApplyRefund"

    //查询余额
    const val QUERY_BALANCE = "api/user/queryBalance"

    //查询积分余额
    const val QUERY_INTEGRAL_BALANCE = "api/user/queryIntegralBalance"

    //下单温馨提示
    const val GET_ORDER_TIP = "api/gasPublic/getOrderTs"

    //创建加油订单
    const val CREATE_ORDER = "api/tiein/v1/refuelAndProductCreateOrder"

    //app版本更新检测
    const val CHECK_VERSION = "api/v1/common/checkVersion"

    //客服中心
    const val CALL_CENTER = "api/v1/common/getCallCenter"

    //退出登录
    const val USER_LOGOUT = "api/v1/wx/logout"

    //油站详情
    const val OIL_DETAIL = "api/gasPublic/getGasStationInfo"

    //首页积分豪礼
    const val HOME_PRODUCT = "api/product/v1/queryProductsByModule"

    //获取系统通知
    const val GET_ARTICLES = "api/v1/common/getArticles"

    //获取订单通知
    const val GET_NOTICES = "api/user/getNotices"

    //获取加油收银台
    const val GET_PAY_TYPE = "api/gasPublic/refuelCashierDesk"

    //收银台优惠标签
    const val GET_PAY_TAG = "api/gasPublic/payTag"

    //加油支付
    const val PAY_ORDER = "api/gasPublic/refuelPayOrder"

    //加油支付结果回调数据
    const val PAY_ORDER_RESULT = "/api/tiein/v1/queryPayOrderResult"

    //搜索权益
    const val QUERY_PRODUCTS_BY_NAME = "api/product/v1/queryProductsByName"

    //热门搜索
    const val HOT_SEARCH = "api/product/v1/hotSearch"

    //查询油站距离
    const val GET_OIL_DISTANCE = "api/gasPublic/getDistance"

    //是否隐藏权益相关内容
    const val GET_OS_OVERALL = "api/v1/common/getOsOverall"

    //是否隐藏余额
    const val GET_OS_BALANCE = "api/v1/common/getOsBalanceShow"

    //加油新老用户区分
    const val IS_NEW_USER = "api/gasPublic/isNewUser"

    //极光ID
    const val GET_JPUSH_ID_URL = "api/v1/user/getJPushId"

    //本地生活 门店列表
    const val GET_STORE_LIST = "api/localLife/getStoreList"

    //本地生活 门店详情
    const val GET_STORE_INFO = "api/localLife/getStoreInfo"

    //本地生活 订单列表
    const val GET_STORE_ORDER_LIST = "api/localLife/orderList"

    //获取油站邀请人专属油站
    const val GET_SPEC_GAS_ID = "api/gasPublic/specGasId"

    //获取月度红包列表
    const val GET_MONTH_COUPON = "api/coupon/v1/getPlatformMonthCouponVOs"

    //获取每周积分签到列表信息
    const val GET_INTEGRAL_INFO = "api/product/v1/getIntegralInfo"

    //每周积分签到
    const val INTEGRAL_SIGN = "api/product/v1/integralSign"

    //电子围栏新接口
    const val GET_PAY_DISTANCE = "api/gasPublic/getPayDistance"

    //数据埋点
    const val TRACKING_ADD = "api/v1/clickData/add"

    //获取月度红包以及月度权益包购买条件信息(单独购买)
    const val GET_MONTH_EQUITY_INFO = "api/monthCard/v1/getMonthEquityInfo"

    //新人礼包
    const val NEW_USER_STATUS = "api/activeCommon/newUserStatus"

    //首页加油任务
    const val QUERY_REFUEL_JOB = "api/activeCommon/queryRefuelJob"

    //领取加油任务红包
    const val RECEIVE_OIL_JOB_COUPON = "api/activeCommon/receiveOilJobCoupon"

    //获取搭售列表
    const val QUERY_SALE_INFO = "api/tiein/v1/queryTieinSaleInfo"

    //获取搭售列表
    const val QUERY_SALE_INFO_CAR_SERVE = "api/tiein/v1/queryTieinSaleCfInfo"

    //获取会员卡信息
    const val GET_MEMBER_CARD_INFO = "api/v1/member/getMemberBuyIndex"

    //获取会员卡有效期
    const val GET_MEMBER_CARD = "api/v1/member/getMemberCard"

    //首页卡片
    const val HOME_CARD_INFO = "api/tiein/v1/queryHomeCardInfo"

    //首页菜单
    const val HOME_MENU_INFO = "api/tiein/v1/queryHomeKingKongDistrict"

    //浮窗接口
    const val DRAG_INFO = "api/v1/member/floatingWindow"
}