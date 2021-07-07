package com.xxjy.common.constants

import android.text.TextUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import java.util.*

/**
 * @author power
 * @date 12/4/20 2:47 PM
 * @project RunElephant
 * @description:
 */
object UserConstants {
    fun setH5Links(links: String?) {
        SPUtils.getInstance().put(SPConstants.WEB_LINKS, links)
    }

    var token: String?
        get() = SPUtils.getInstance().getString(SPConstants.APP_TOKEN, "")
        set(token) {
            SPUtils.getInstance().put(SPConstants.APP_TOKEN, token)
        }
    var userType: Int
        get() = SPUtils.getInstance().getInt(SPConstants.USER_TYPE, -1)
        set(userType) {
            SPUtils.getInstance().put(SPConstants.USER_TYPE, userType)
        }
    var mobile: String?
        get() = SPUtils.getInstance().getString(SPConstants.MOBILE, "")
        set(mobile) {
            SPUtils.getInstance().put(SPConstants.MOBILE, mobile)
        }
    val uuid: String
        get() {
            val uniqueDeviceId: String = DeviceUtils.getUniqueDeviceId()
            return if (StringUtils.isEmpty(uniqueDeviceId)) {
                ""
            } else uniqueDeviceId
        }
    var openId: String?
        get() = SPUtils.getInstance().getString(SPConstants.OPEN_ID)
        set(openId) {
            SPUtils.getInstance().put(SPConstants.OPEN_ID, "")
        }
    var isLogin: Boolean?
        get() = SPUtils.getInstance().getBoolean(SPConstants.LOGIN_STATUS)
        set(isLogin) {
            SPUtils.getInstance().put(SPConstants.LOGIN_STATUS, true)
        }
    var longitude: String?
        get() = SPUtils.getInstance().getString(SPConstants.LONGITUDE, "0")
        set(longitude) {
            SPUtils.getInstance().put(SPConstants.LONGITUDE, longitude)
        }
    var latitude: String?
        get() = SPUtils.getInstance().getString(SPConstants.LATITUDE, "0")
        set(latitude) {
            SPUtils.getInstance().put(SPConstants.LATITUDE, latitude)
        }
    var firstOpen: Boolean
        get() = SPUtils.getInstance().getBoolean(SPConstants.FIRST_OPEN)
        set(firstOpen) {
            SPUtils.getInstance().put(SPConstants.FIRST_OPEN, firstOpen)
        }
    var agreePrivacy: Boolean
        get() = SPUtils.getInstance().getBoolean(SPConstants.AGREE_PRIVACY)
        set(isAgree) {
            SPUtils.getInstance().put(SPConstants.AGREE_PRIVACY, isAgree)
        }
    var appChannel: String?
        get() = SPUtils.getInstance().getString(SPConstants.APP_CHANNEL_KEY)
        set(appChannel) {
            SPUtils.getInstance().put(SPConstants.APP_CHANNEL_KEY, appChannel)
        }
    var goneIntegral: Boolean
        get() = SPUtils.getInstance().getBoolean(SPConstants.GONE_INTEGRAL, false)
        set(b) {
            SPUtils.getInstance().put(SPConstants.GONE_INTEGRAL, b)
        }
    var goneBalance: Boolean
        get() = SPUtils.getInstance().getBoolean(SPConstants.GONE_BALANCE, false)
        set(b) {
            SPUtils.getInstance().put(SPConstants.GONE_BALANCE, b)
        }
    var backgroundTime: Long
        get() = SPUtils.getInstance().getLong(SPConstants.BACKGROUND_TIME)
        set(b) {
            SPUtils.getInstance().put(SPConstants.BACKGROUND_TIME, b)
        }
    var startFrom: String?
        get() = SPUtils.getInstance().getString(SPConstants.START_FROM)
        set(startFrom) {
            SPUtils.getInstance().put(SPConstants.START_FROM, startFrom)
        }
    var notificationRemind: Boolean
        get() {
            val content = SPUtils.getInstance().getString(SPConstants.NOTIFICATION_REMIND)
            return TextUtils.equals(
                content,
                TimeUtils.date2String(Date(), "yyyy-MM-dd") + "@_@" + true
            )
        }
        set(b) {
            val str = TimeUtils.date2String(Date(), "yyyy-MM-dd") + "@_@" + b
            SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND, str)
        }
//    var notificationRemindVersion: Boolean
//        get() {
//            val content = SPUtils.getInstance().getString(SPConstants.NOTIFICATION_REMIND_VERSION)
//            return TextUtils.equals(content, Util.getVersionName().toString() + "@_@" + true)
//        }
//        set(b) {
//            val str: String = Util.getVersionName().toString() + "@_@" + b
//            SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND_VERSION, str)
//        }

    fun setShowNewUserRedPacket() {
        SPUtils.getInstance().put(SPConstants.NEW_USER_RED_PACKET, System.currentTimeMillis())
    }

    val showNewUserRedPacket: Boolean
        get() {
            val content = SPUtils.getInstance().getLong(SPConstants.NEW_USER_RED_PACKET, 0L)
            return TimeUtils.isToday(content)
        }
    var notificationRemindUserCenter: Boolean
        get() = SPUtils.getInstance().getBoolean(SPConstants.NOTIFICATION_REMIND_USER_CENTER, false)
        set(b) {
            SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND_USER_CENTER, b)
        }
    var carType: Int
        get() = SPUtils.getInstance().getInt(SPConstants.CAR_TYPE, -1)
        set(carType) {
            SPUtils.getInstance().put(SPConstants.CAR_TYPE, carType)
        }

    //定位信息
//    val location: String
//        get() = if (MapLocationHelper.getLocationLatitude() !== 0 && MapLocationHelper.getLocationLongitude() !== 0) {
//            MapLocationHelper.getLocationLongitude()
//                .toString() + "," + MapLocationHelper.getLocationLatitude()
//        } else {
//            ""
//        }
//
//    /**
//     * @return 城市编码
//     */
//    val cityCode: String
//        get() = if (MapLocationHelper.getAdCode() != null) {
//            MapLocationHelper.getAdCode().replace(
//                MapLocationHelper.getAdCode().substring(MapLocationHelper.getAdCode().length() - 2),
//                "00"
//            )
//        } else {
//            ""
//        }
//
//    /**
//     * @return 区域编码
//     */
//    val adCode: String
//        get() = if (MapLocationHelper.getAdCode() != null) {
//            MapLocationHelper.getAdCode()
//        } else {
//            ""
//        }
}