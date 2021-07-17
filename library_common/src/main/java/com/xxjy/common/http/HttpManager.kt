package com.xxjy.common.http

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.xxjy.common.BuildConfig
import com.xxjy.common.constants.ApiService
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.UserConstants
import okhttp3.OkHttpClient
import rxhttp.RxHttp
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.callback.Function
import rxhttp.wrapper.cookie.CookieStore
import rxhttp.wrapper.param.Method
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


/**
 * @author power
 * @date 12/1/20 2:18 PM
 * @project RunElephant
 * @description:
 */
object HttpManager {
    @SuppressLint("NewApi")
    fun init(context: Application) {
        val file = File(context.externalCacheDir, "RxHttpCookie")
        val sslParams: HttpsUtils.SSLParams = HttpsUtils.getSslSocketFactory()
        val client: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(CookieStore(file))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true }) //忽略host验证
            .addInterceptor(LoggerInterceptor.loggingIntercaptor) //                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            //                .addInterceptor(new RedirectInterceptor())
            //                  .addInterceptor(new TokenInterceptor())
            .build()

        //设置缓存策略，非必须
        val cacheFile = File(context.externalCacheDir, "RxHttpCache")

        //RxHttp初始化，自定义OkHttpClient对象，非必须
        RxHttpPlugins.init(client)
            .setDebug(BuildConfig.DEBUG)
            .setCache(cacheFile, 1000 * 100, CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE)
            .setExcludeCacheKeys("time")//设置一些key，不参与cacheKey的组拼
            .setOnParamAssembly {
                //设置公共参数，非必须
                /*根据不同请求添加不同参数，子线程执行，每次发送请求前都会被回调
                 *如果希望部分请求不回调这里，发请求前调用Param.setAssemblyEnabled(false)即可
                 */
                val method: Method = it.method
                if (method.isGet) { //Get请求
                } else if (method.isPost) { //Post请求
                }
                val token: String = UserConstants.token
                if (!TextUtils.isEmpty(token)) {
                    //添加公共请求头
                    it.setHeader("Authorization", "Bearer $token")
                }
                it.setHeader("User-Agent", "android")

                //添加公共参数
                it.setAllHeader(getCommonParams(it))
            }
        //设置数据解密/解码器，非必须
//        RxHttp.setResultDecoder(s -> s);

        //设置全局的转换器，非必须
//        RxHttp.setConverter(FastJsonConverter.create());
    }

    /**
     * 生成最终的请求体的参数,主要用来添加默认的 参数
     *
     * @param p
     * @return 最终参数
     */
    fun getCommonParams(p: Param<*>?): MutableMap<String, String> {
        val finalParams: MutableMap<String, String> = HashMap()
        val t = System.currentTimeMillis().toString() + "" //时间戳
        // XIAOMI MI5 8.0
        val osv = DeviceUtils.getManufacturer() + " " + DeviceUtils.getModel() + " " +
                DeviceUtils.getSDKVersionName() //操作系统版本号
        val cv = AppUtils.getAppVersionName() //客户端版本号
        var location = UserConstants.getLocation()//位置信息

        var cityCode = UserConstants.getCityCode()
        var adCode = UserConstants.getAdCode()

        val app_store: String = UserConstants.app_channel_key
        val openId: String = UserConstants.openId
        finalParams["t"] = t //时间戳
        finalParams["os"] = "1" //操作系统
        finalParams["cv"] = cv //客户端版本号
        if (p != null) {
            finalParams["method"] =
                p.simpleUrl.substring(if (Constants.URL_IS_DEBUG) ApiService.DEBUG_URL.length - 1 else ApiService.RELEASE_URL.length - 1)
            val sign: String? = HeaderUtils.getSign(
                HeaderUtils.sortMapByKey(finalParams)
            )
            sign?.let { finalParams["sign"] = sign }
            finalParams.remove("method")
            finalParams.remove("did")
        } else {
            finalParams["did"] = DeviceUtils.getUniqueDeviceId()
        }
        if (TextUtils.isEmpty(openId)) {
            finalParams["openId"] = ""
        } else {
            finalParams["openId"] = openId
        }
        finalParams["osv"] = osv //手机系统版本
        finalParams["location"] = location //位置信息
        finalParams["cityCode"] = cityCode //城市编码
        finalParams["region"] = cityCode //banner用的城市编码
        finalParams["adCode"] = adCode //区域编码
        finalParams["appStore"] = app_store //下载渠道
        finalParams["appId"] = "Orvay1rVsoU9nlpY"
//        finalParams["deviceId"] = SmAntiFraudManager.getDeviceId() //数美风控deviceId
        return finalParams
    }
}