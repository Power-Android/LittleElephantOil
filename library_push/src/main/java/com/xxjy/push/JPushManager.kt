package com.xxjy.push

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.xxjy.push.BuildConfig

/**
 * 极光推送的管理器
 */
object JPushManager {
    /**
     * 初始化极光推送sdk相关，包括推送的初始化和统计的初始化
     *
     * @param application
     */
    fun initJPush(application: Context) {
        initPush(application)
        //        initAnalytics(application);
    }

    //初始化推送
    private fun initPush(application: Context) {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(application)
    }

    /**
     * 统计界面开始
     *
     * @param context
     * @param pageName
     */
    fun onPageStart(context: Context?, pageName: String?) {
//        JAnalyticsInterface.onPageStart(context, pageName);
    }

    /**
     * 统计界面结束
     *
     * @param context
     * @param pageName
     */
    fun onPageEnd(context: Context?, pageName: String?) {
//        JAnalyticsInterface.onPageEnd(context, pageName);
    }

    // TODO: 2021/7/15 上报极光id 
    //post极光regestId;
//    fun postJPushdata() {
//        val registrationId: String = JPushInterface.getRegistrationID(App.getContext())
//        val map: HashMap<String?, String?> = HashMap<Any?, Any?>()
//        map["registrationId"] = registrationId
//        LogUtils.e(registrationId)
//        RxHttp.postForm(ApiService.GET_JPUSH_ID_URL, map)
//            .asResponse(Response::class.java)
//            .subscribe(Consumer<Any?> { })
//    }
}