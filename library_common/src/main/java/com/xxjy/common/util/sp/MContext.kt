package com.xxjy.common.util.sp

import android.content.Context

/**
 * 创建日期：2021/7/14 11:28
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.common.util.sp
 * 类说明：
 */

object MContext{
    private lateinit var application : Context
     fun init(context : Context){
        application= context
    }
    fun context() : Context{
        return application
    }
}