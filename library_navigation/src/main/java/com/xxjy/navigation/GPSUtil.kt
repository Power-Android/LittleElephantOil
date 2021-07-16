package com.xxjy.navigation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2017/9/19.
 */
object GPSUtil {
    /**
     * 查看是否可以定位
     *
     * @return
     */
    fun isOpenGPS(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network: Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return enabled || network
    }

    /**
     * 强制帮助用户打开gps定位
     *
     * @param context
     */
    fun openGPS(context: Context?) {
        val GPSIntent = Intent()
        GPSIntent.setClassName(
            "com.android.settings",
            "com.android.settings.widget.SettingsAppWidgetProvider"
        )
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.setData(Uri.parse("custom:3"))
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }
    }

}