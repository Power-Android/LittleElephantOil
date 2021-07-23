package com.xxjy.web

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.WebView
import java.io.File

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

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(webView: WebView) {
        val webSetting = webView.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true

        //有图：正常加载显示所有图片 https://x5.tencent.com/docs/webview.html
        webSetting.loadsImagesAutomatically = true
        webSetting.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            //            webSetting.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.cacheMode = com.tencent.smtt.sdk.WebSettings.LOAD_DEFAULT
        webSetting.defaultTextEncodingName = "utf-8"
//        webView.clearCache(true);
    }

    fun initWebView(context: Context, webView: WebView) {
        initWebView(webView)
        val webSetting = webView.settings
        webSetting.setAppCacheEnabled(true)
        val webCacheFile = File(context.cacheDir, "web_cache")
        val webDataFile = File(context.cacheDir, "web_database")
        val webGeoLocFile = File(context.cacheDir, "web_geo_loc")
        webSetting.setAppCachePath(webCacheFile.path)
        webSetting.databasePath = webDataFile.path
        webSetting.setGeolocationDatabasePath(webGeoLocFile.path)
    }


}