package com.xxjy.common.util

import android.content.Context
import com.xxjy.common.provide.MContext.context
import com.xxjy.common.util.AppManager
import android.content.Intent
import com.xxjy.common.provide.MContext
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.AppUtils
import com.xxjy.common.base.BaseActivity
import java.io.File

/**
 * @Description:APP管理类
 * @date 2015-4-11
 */
object AppManager {
    /**
     * 打开安装包
     *
     * @param activity
     * @param apkFile
     */
    fun installApp(activity: BaseActivity, apkFile: File?) {
        // 核心是下面几句代码
        if (apkFile == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val hasInstallPermission = activity.packageManager.canRequestPackageInstalls()
                if (hasInstallPermission) {
                    AppUtils.installApp(apkFile)
                } else {
                    //请求安装未知应用来源的权限
//                    ActivityCompat.requestPermissions(activity,
//                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 0);
                    activity.showToastWarning("请赋予权限进行安装新版本")
                    startInstallPermissionSettingActivity(activity)
                }
            } else {
                AppUtils.installApp(apkFile)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            activity.showToastWarning(e.message)
            //            activity.showToastWarning("安装失败,请自行跳转应用市场进行更新");
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     *
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity(activity: BaseActivity) {
        //注意这个是8.0新API
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    /**
     * 获取 app 中的 渠道 channel 值
     *
     * @return
     */
    val appMetaChannel: String?
        get() = getAppMetaData(context(), "UMENG_CHANNEL")

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    fun getAppMetaData(ctx: Context, metaName: String?): String? {
        var resultData: String? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                val applicationInfo =
                    packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(metaName)
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return resultData
    }
}