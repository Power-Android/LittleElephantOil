package com.xxjy.common.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

object NotificationsUtils {
    /**
     * 判断是否开启通知
     * @param context
     * @return
     */
    fun isNotificationEnabled(context: Context?): Boolean {
        return NotificationManagerCompat.from(context!!).areNotificationsEnabled()
    }

    /**
     * 通知权限申请
     * @param context
     */
    fun requestNotify(context: Context) {
        /**
         * 跳到通知栏设置界面
         * @param context
         */
        val localIntent = Intent()
        ///< 直接跳转到应用通知设置的代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O
        ) {
            localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            localIntent.putExtra("app_package", context.packageName)
            localIntent.putExtra("app_uid", context.applicationInfo.uid)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            localIntent.addCategory(Intent.CATEGORY_DEFAULT)
            localIntent.data = Uri.parse("package:" + context.packageName)
        } else {
            ///< 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", context.packageName, null)
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName(
                    "com.android.settings",
                    "com.android.setting.InstalledAppDetails"
                )
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
            }
        }
        context.startActivity(localIntent)
    }
}