package com.xxjy.common.provide

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.viewbinding.BuildConfig
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.ToastUtils
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.EventConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.http.HttpManager
import com.xxjy.common.util.AppManager
import com.xxjy.common.util.ForegroundCallbacks
import kotlin.concurrent.thread

/**
 * 创建日期：2021/7/14 11:28
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.common.util.sp
 * 类说明：
 */

object MContext {
    private lateinit var application: Context

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, _: RefreshLayout? ->
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, _: RefreshLayout? ->
            ClassicsFooter(context).setFinishDuration(0) //.setDrawableSize(20);
        }
    }

    fun init(context: Context) {
        application = context
        initSDK()
    }

    fun context(): Context {
        return application
    }

    private fun initSDK() {
            var appChannel = "" //标记app的渠道
            //QMUI
            QMUISwipeBackActivityManager.init(application as Application)

            if (BuildConfig.DEBUG) {
                CrashUtils.init { crashInfo: CrashUtils.CrashInfo? ->
                    ToastUtils.showLong(
                        "崩溃日志已存储至目录！"
                    )
                }
            }
            //网络请求Rxhttp
            HttpManager.init(application as Application)

            appChannel = UserConstants.app_channel_key
            if (TextUtils.isEmpty(appChannel)) {
                appChannel = AppManager.appMetaChannel.toString()
                UserConstants.app_channel_key = appChannel
            }

//            //初始化闪验sdk
//            ShanYanManager.initShanYnaSdk(this@MainActivity)
//            //标记首次进入app埋点
//            UMengOnEvent.onFirstStartEvent()
//
//            //极光推送配置
//            JPushManager.initJPush(this@MainActivity)
//            //友盟统计
//            UMengManager.init(this@MainActivity)
//            //数美风控
//            SmAntiFraudManager.initSdk(this@MainActivity)

            initAppStatusListener()
    }

    @SuppressLint("NewApi")
    private fun initAppStatusListener() {
        ForegroundCallbacks.init(application as Application)?.addListener(object :
            ForegroundCallbacks.Listener {
            override fun onBecameForeground() {
                if (!TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {
                    val backgroundTime: Long = UserConstants.background_time as Long
                    if (System.currentTimeMillis() - backgroundTime > 300000) {
                        Constants.HUNTER_GAS_ID = ""
                        BusUtils.postSticky(
                            EventConstants.EVENT_JUMP_HUNTER_CODE,
                            Constants.HUNTER_GAS_ID
                        )
                    }
                }
            }

            override fun onBecameBackground() {
                if (!TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {
                    UserConstants.background_time = System.currentTimeMillis().toString()
                }
            }
        })
    }
}