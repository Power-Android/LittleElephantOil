package com.xxjy.common.router

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.xxjy.common.BuildConfig

/**
 * 创建日期：2021/7/16 17:56
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.common.router
 * 类说明：
 */
object ARouterManager {

    fun init(app: Application) {
        ///初始化路由
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(app)

    }

}