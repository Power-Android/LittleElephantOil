package com.xxjy.common.constants

interface CarServeApiService {
    companion object {
        //默认正式服务器url
        const val CONFIG_RELEASE_URL = "https://api.xiaoxiangjiayou.com/"
        const val RELEASE_URL = CONFIG_RELEASE_URL

        //默认测试服务器url
        const val CONFIG_DEBUG_URL = "https://api-dev.xiaoxiangjiayou.com/"

        //    public static final String CONFIG_DEBUG_URL = "https://ccore.qqgyhk.com/";
        const val DEBUG_URL = CONFIG_DEBUG_URL
        val BASE_URL = if (Constants.URL_IS_DEBUG) DEBUG_URL else RELEASE_URL
        val APP_ID = if (Constants.URL_IS_DEBUG) "Orvay1rVsoU9nlpY" else "Orvay1rVsoU9nlpY"

        //门店省市区级联
        val GET_AREA = BASE_URL + "cs/api/v1/admin/area/"

        //客户渠道商品服务类型分类
        val GET_PRODUCT_CATEGORY =
            BASE_URL + "cs/api/v1/customer/product/category/product/category/list"

        //客户渠道门店
        val GET_STORE_LIST = BASE_URL + "cs/api/v1/customer/channel/store/list"

        //客户渠道门店信息详情/api/v1/customer/channel/store/{no}/{appid}
        val GET_STORE_DETAILS = BASE_URL + "cs/api/v1/customer/channel/store/"

        //当前可用优惠券分页列表-门店详情使用
        val COUPON_USABLE = BASE_URL + "cs/api/v1/customer/coupon/list/usable"

        //优惠券列表
        val COUPON_LIST = BASE_URL + "cs/api/v1/customer/coupon/list"

        //创建车服订单
        val COMMIT_ORDER = BASE_URL + "oil/api/v1/customer/car/createOrder"

        //车服订单列表
        val GET_ORDER_LIST = BASE_URL + "oil/api/v1/customer/car/order/list"

        //车服订单取消
        val CANCEL_ORDER = BASE_URL + "oil/api/v1/customer/car/order/cancel/tiein"

        //查询车服搭售加油卡信息：入口,商品
        val TYING_PRODUCT = BASE_URL + "api/tiein/v1/queryTieinSaleCfInfo"

        //    String TYING_PRODUCT = "http://192.168.1.84:8833/api/tiein/v1/queryTieinSaleCfInfo";
        //收银台payment cashier
        val PAYMENT_CASHIER = BASE_URL + "oil/api/v1/customer/pay/method"

        //订单支付
        val PAYMENT_ORDER = BASE_URL + "oil/api/v1/customer/pay/payment"

        //获取订单支付结果
        val PAYMENT_RESULT = BASE_URL + "oil/api/v1/customer/pay/payment/result"

        //车服订单详情
        val ORDER_INFO = BASE_URL + "oil/api/v1/customer/car/order/detail/"

        //首页常去车服门店
        val OFENT_CAR_SERVE = BASE_URL + "cs/api/v1/customer/channel/store/use/list"

        //车型
        val GET_CAR_TYPE = BASE_URL + "cs/api/v1/customer/channel/store/cartype/list"

        //海报
        val GET_POSTER = BASE_URL + "cs/api/v1/admin/customer/channel/poster/list"
    }
}