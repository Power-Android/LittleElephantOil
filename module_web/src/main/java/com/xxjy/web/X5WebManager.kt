package com.xxjy.web

import afu.org.checkerframework.checker.igj.qual.Mutable
import android.app.Application
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import java.util.*

/**
 * @author power
 * @date 12/3/20 2:10 PM
 * @project RunElephant
 * @description:
 */
object X5WebManager {
    /**
     *
     */
    fun initX5Web(application: Application?) {
        val map: MutableMap<String, Boolean> = mutableMapOf()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map.toMap())
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(application, cb)
    }
}