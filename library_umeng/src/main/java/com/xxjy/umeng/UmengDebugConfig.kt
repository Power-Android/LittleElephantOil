package com.xxjy.umeng

import android.content.Context
import com.umeng.commonsdk.statistics.common.DeviceConfig

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2018/4/16.
 */
object UmengDebugConfig {
    @JvmStatic
    fun getTestDeviceInfo(context: Context?): String {
        val deviceInfo = arrayOfNulls<String>(2)
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context)
                deviceInfo[1] = DeviceConfig.getMac(context)
            }
        } catch (e: Exception) {
        }
        return "{\"device_id\":\"" + deviceInfo[0] + "\",\"mac\":\"" + deviceInfo[1] + "\"}"
    }
}