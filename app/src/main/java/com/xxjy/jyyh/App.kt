package com.xxjy.jyyh

import android.app.Application
import android.content.Context
import com.xxjy.common.router.ARouterManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * 创建日期：2021/7/6 10:29
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.jyyh
 * 类说明：
 */
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        ARouterManager.init(this)
    }

}