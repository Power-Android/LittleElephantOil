package com.xxjy.umeng

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig

/**
 * umeng 管理器
 */
object UMengManager {
    /**
     * 初始化umeng统计
     *
     * @param context
     */
    fun init(context: Context) {
        //友盟分享
        PlatformConfig.setWeixin(BuildConfig.WX_APP_ID, BuildConfig.WX_APP_SCRIPT)
        PlatformConfig.setWXFileProvider("com.xxjy.jyyh.file.provider")
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null)

        //使用auto模式不再需要Activity中的代码埋点
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        setDebug(context, BuildConfig.DEBUG)
        setCatchUncaughtExceptions(!BuildConfig.DEBUG)
    }

    /**
     * 是否调试状态
     */
    private fun setDebug(context: Context, isDebug: Boolean) {
        UMConfigure.setLogEnabled(isDebug)
        if (isDebug) {
            val testDeviceInfo = UmengDebugConfig.getTestDeviceInfo(context)
        }
    }

    fun getChannelName(context: Context?): String? {
        if (context == null) {
            return null
        }
        var channelName: String? = null
        try {
            val packageManager: PackageManager? = context.packageManager
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData.get("UMENG_CHANNEL").toString() + ""
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return channelName
    }

    /**
     * 调用kill或者exit之类的方法杀死进程，请务必在此之前调用onKillProcess(Context context)方法，用来保存统计数据。
     *
     * @param context
     */
    fun onKillProcess(context: Context?) {
        MobclickAgent.onKillProcess(context)
    }

    /**
     * 在每个Activity的onResume方法中调用 MobclickAgent.onResume(Context),
     *
     *
     * onPause方法中调用 MobclickAgent.onPause(Context)
     *
     * @param context
     */
    fun onResume(context: AppCompatActivity?) {
        MobclickAgent.onResume(context);
    }

    fun onPause(context: AppCompatActivity?) {
        MobclickAgent.onPause(context);
    }

    /**
     * 需要统计应用自身的账号登录情况，请使用以下接口：
     *
     * @param id
     */
    fun onProfileSignIn(id: String?) {
        MobclickAgent.onProfileSignIn("userID")
    }

    /**
     * 账号登出时需调用此接口，调用之后不再发送账号相关内容。
     */
    fun onProfileSignOff() {
        MobclickAgent.onProfileSignOff()
    }

    /**
     * SDK通过Thread.UncaughtExceptionHandler 捕获程序崩溃日志，并在程序下次启动时发送到服务器。
     * 如不需要错误统计功能，可通过此方法关闭
     *
     * @param b
     */
    fun setCatchUncaughtExceptions(b: Boolean) {
        MobclickAgent.setCatchUncaughtExceptions(b)
    }

    /**
     * 如果开发者自己捕获了错误，需要上传到友盟+服务器可以调用下面方法：
     *
     * @param mContext
     * @param ex
     */
    fun reportError(mContext: Context?, ex: Throwable?) {
        if (!BuildConfig.DEBUG) {
            MobclickAgent.reportError(mContext, ex) //异常发送友盟服务器
        }
    }
}