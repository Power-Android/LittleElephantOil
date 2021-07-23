package com.xxjy.web.application

import android.app.Application
import com.xxjy.common.router.ARouterManager
import com.xxjy.umeng.UMengManager
import com.xxjy.web.X5WebManager

/**
 * 创建日期：2021/7/21 13:55
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.web.application
 * 类说明：
 */
class WebApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        X5WebManager.initX5Web(this)
        ARouterManager.init(this)
        UMengManager.init(this)
    }
}