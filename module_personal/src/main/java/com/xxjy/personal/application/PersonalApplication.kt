package com.xxjy.personal.application

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.xxjy.common.router.ARouterManager
import com.xxjy.umeng.UMengManager

/**
 * 创建日期：2021/7/20 17:34
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.personal.application
 * 类说明：
 */
class PersonalApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ARouterManager.init(this)
        UMengManager.init(this)
    }
}