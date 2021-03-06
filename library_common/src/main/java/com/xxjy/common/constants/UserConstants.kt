package com.xxjy.common.constants

import android.annotation.SuppressLint
import android.text.TextUtils
import com.blankj.utilcode.util.TimeUtils
import com.xxjy.common.provide.MContext.context
import com.xxjy.common.util.SharedPreferencesUtils

/**
 * @author power
 * @date 12/4/20 2:47 PM
 * @project RunElephant
 * @description:
 */
@SuppressLint("StaticFieldLeak")
object UserConstants {

    var app_channel_key by SharedPreferencesUtils(context(), SPConstants.APP_CHANNEL_KEY, "")
    var first_open by SharedPreferencesUtils(context(), SPConstants.FIRST_OPEN, "")
    var agree_privacy by SharedPreferencesUtils(context(), SPConstants.AGREE_PRIVACY, false)
    var splash_screen_ad by SharedPreferencesUtils(context(), SPConstants.SPLASH_SCREEN_AD, "")
    var login_status by SharedPreferencesUtils(context(), SPConstants.LOGIN_STATUS, false)
    var token by SharedPreferencesUtils(context(), SPConstants.APP_TOKEN, "")
    var user_type by SharedPreferencesUtils(context(), SPConstants.USER_TYPE, -1)
    var mobile by SharedPreferencesUtils(context(), SPConstants.MOBILE, "")
    var web_links by SharedPreferencesUtils(context(), SPConstants.WEB_LINKS, "")
    var longitude by SharedPreferencesUtils(context(), SPConstants.LONGITUDE, "0")
    var latitude by SharedPreferencesUtils(context(), SPConstants.LATITUDE, "0")
    var openId by SharedPreferencesUtils(context(), SPConstants.OPEN_ID, "")
    var city_code by SharedPreferencesUtils(context(), SPConstants.AD_CODE, "")
    var ad_code by SharedPreferencesUtils(context(), SPConstants.CITY_CODE, "")
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
        false
    )
    var notification_remind_version by SharedPreferencesUtils(
        context(),
        SPConstants.NOTIFICATION_REMIND_VERSION,
        false
    )
    var notification_remind_user_center by SharedPreferencesUtils(
        context(),
        SPConstants.NOTIFICATION_REMIND_USER_CENTER,
        false
    )
    var startFrom by SharedPreferencesUtils(context(), SPConstants.START_FROM, "")
    var new_user_red_packet by SharedPreferencesUtils(
        context(),
        SPConstants.NEW_USER_RED_PACKET,
        0L
    )

    fun isNewUserRedPacket(): Boolean{
        return TimeUtils.isToday(new_user_red_packet)
    }

    var car_type by SharedPreferencesUtils(context(), SPConstants.CAR_TYPE, -1)

    //????????????
    fun getLocation(): String {
        return if (latitude?.toDouble() != 0.0 && longitude?.toDouble() != 0.0) {
            "$longitude,$latitude"
        } else {
            ""
        }
    }

    /**
     * @return ????????????
     */
    fun getCityCode(): String {
        return if (!TextUtils.isEmpty(ad_code)) {
            ad_code!!.replace(
                ad_code!!.substring(ad_code!!.length - 2),
                "00"
            )
        } else {
            ""
        }
    }

    /**
     * @return ????????????
     */
    fun getAdCode(): String {
        return if (ad_code != null) ad_code as String else ""

    }

}