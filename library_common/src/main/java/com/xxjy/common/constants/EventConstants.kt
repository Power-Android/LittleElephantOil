package com.xxjy.common.constants

/**
 * @author power
 * @date 12/1/20 1:50 PM
 * @project RunElephant
 * @description: eventBus存储tag用
 */
object EventConstants {
    const val EVENT_LOGIN_INFO = "event_Login"
    const val EVENT_REFRESH_HOME = "event_refresh_home"
    const val EVENT_TO_OIL_FRAGMENT = "event_to_oilFragment"
    const val EVENT_TO_INTEGRAL_FRAGMENT = "event_to_integralFragment"
    const val EVENT_TO_HOME_FRAGMENT = "event_to_homeFragment"
    const val EVENT_TO_CAR_FRAGMENT = "event_to_carFragment"
    const val EVENT_CHANGE_FRAGMENT = "event_change_fragment"

    //支付确认页回调
    const val EVENT_JUMP_PAY_QUERY = "event_jump_pay_query"

    //登录填写猎人码跳转
    const val EVENT_JUMP_HUNTER_CODE = "event_jump_hunter_code"
}