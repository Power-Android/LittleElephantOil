package com.xxjy.common.constants

import com.xxjy.common.util.sp.MContext.context
import com.xxjy.common.util.sp.SharedPreferencesUtils

/**
 * @author power
 * @date 12/4/20 2:47 PM
 * @project RunElephant
 * @description:
 */
object UserConstants {

    var app_channel_key by SharedPreferencesUtils(context(), SPConstants.APP_CHANNEL_KEY, "")
    var first_open by SharedPreferencesUtils(context(), SPConstants.FIRST_OPEN, "")
    var agree_privacy by SharedPreferencesUtils(context(), SPConstants.AGREE_PRIVACY, "")
    var login_status by SharedPreferencesUtils(context(), SPConstants.LOGIN_STATUS, "")
    var token by SharedPreferencesUtils(context(), SPConstants.APP_TOKEN, "")
    var user_type by SharedPreferencesUtils(context(), SPConstants.USER_TYPE, -1)
    var mobile by SharedPreferencesUtils(context(), SPConstants.MOBILE, "")
    var web_links by SharedPreferencesUtils(context(), SPConstants.WEB_LINKS, "")
    var longitude by SharedPreferencesUtils(context(), SPConstants.LONGITUDE, "0")
    var latitude by SharedPreferencesUtils(context(), SPConstants.LATITUDE, "0")
    var openId by SharedPreferencesUtils(context(), SPConstants.OPEN_ID, "")
    var city_code by SharedPreferencesUtils(context(), SPConstants.CITY_CODE, "")
    var guide_version by SharedPreferencesUtils(
        context(),
        SPConstants.SP_APP_SHOW_GUIDE_VERSION,
        ""
    )
    var gone_integral by SharedPreferencesUtils(context(), SPConstants.GONE_INTEGRAL, false)
    var gone_balance by SharedPreferencesUtils(context(), SPConstants.GONE_BALANCE, false)
    var background_time by SharedPreferencesUtils(context(), SPConstants.BACKGROUND_TIME, "")
    var notification_remind by SharedPreferencesUtils(
        context(),
        SPConstants.NOTIFICATION_REMIND,
        ""
    )
    var notification_remind_version by SharedPreferencesUtils(
        context(),
        SPConstants.NOTIFICATION_REMIND_VERSION,
        ""
    )
    var notification_remind_user_center by SharedPreferencesUtils(
        context(),
        SPConstants.NOTIFICATION_REMIND_USER_CENTER,
        ""
    )
    var startFrom by SharedPreferencesUtils(context(), SPConstants.START_FROM, "")
    var new_user_red_packet by SharedPreferencesUtils(
        context(),
        SPConstants.NEW_USER_RED_PACKET,
        0L
    )
    var is_today by SharedPreferencesUtils(context(), SPConstants.IS_TODAY, "")
    var car_type by SharedPreferencesUtils(context(), SPConstants.CAR_TYPE, -1)

}