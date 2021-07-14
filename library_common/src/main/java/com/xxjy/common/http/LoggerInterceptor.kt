package com.xxjy.common.http

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.rxjava.BuildConfig
import com.xxjy.common.constants.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform.Companion.INFO

/**
 * @author power
 * @date 12/1/20 3:18 PM
 * @project RunElephant
 * @description:
 */
object LoggerInterceptor {
    /**
     * 得到一个 Interceptor 类型的 logger 拦截器,用来进行 打印拦截
     *
     * @return
     */
    val loggingIntercaptor: Interceptor
        get() {
            val loggingInterceptor = LoggingInterceptor.Builder()
            loggingInterceptor
                .setLevel(Level.BASIC)
                .log(INFO)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
            return loggingInterceptor.build()
        }
}