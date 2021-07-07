package com.xxjy.shanyan

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.chuanglan.shanyan_sdk.OneKeyLoginManager
import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener

/**
 * ---------------------------
 * 闪验的管理器
 *
 *
 * https://flash.253.com/document/details?cid=93&lid=519&pc=28&pn=%25E9%2597%25AA%25E9%25AA%258CSDK#43f0e755
 */
object ShanYanManager {
    private const val APP_ID = "KlRtGJtQ" //okWJjwEH
    private const val INIT_SUCCESS_CODE = 1022 //初始化成功的code
    const val DEFAULT_SUCCESS_CODE = 1022 //默认的流程成功code
    private var initCode = 0
    var isShanYanSupport = false //闪验是否支持
        private set

    /**
     * 初始化sdk ,第一步调用
     *
     * @param context
     */
    fun initShanYnaSdk(context: Context) {
        if (!isInitSuccess) {
            //闪验SDK配置debug开关 （必须放在初始化之前，开启后可打印闪验SDK更加详细日志信息）
            OneKeyLoginManager.getInstance().setDebug(BuildConfig.DEBUG)

            //闪验SDK初始化（建议放在Application的onCreate方法中执行）
            initShanyanSDK(context)
        }
    }

    /**
     * 判断是否已经初始化成功
     *
     * @return
     */
    val isInitSuccess: Boolean
        get() = initCode == INIT_SUCCESS_CODE

    /**
     * 检查闪验是否支持
     */
    fun checkShanYanSupportState() {
        if (!isInitSuccess) {
            return
        }
        if (isShanYanSupport) {
            return
        }
        getPhoneInfo { code, result ->
            LogUtils.d("初始化： code==$code   result==$result")
            isShanYanSupport = code == DEFAULT_SUCCESS_CODE
        }
    }

    /**
     * 预取号 ,第二步调用
     *
     * @param listener 结果监听
     */
    fun getPhoneInfo(listener: GetPhoneInfoListener?) {
        OneKeyLoginManager.getInstance().getPhoneInfo(listener)
    }

    /**
     * 关闭闪验界面
     */
    fun finishAuthActivity() {
        OneKeyLoginManager.getInstance().finishAuthActivity()
        OneKeyLoginManager.getInstance().removeAllListener()
        // TODO: 2021/7/7  关闭闪验调用页面
//        ActivityUtils.finishActivity(LoginActivity.class);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private fun initShanyanSDK(context: Context) {
        OneKeyLoginManager.getInstance().init(context, APP_ID) { code, result -> //闪验SDK初始化结果回调
            LogUtils.w("初始化： code==$code   result==$result")
            initCode = code
        }
    }
}