package com.xxjy.common.util

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.xxjy.common.base.BaseActivity

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2018/8/7.
 *
 *
 * 主要用来处理跳转第三方地图的处理
 */
object MapIntentUtils {
    const val BAIDU_PKG = "com.baidu.BaiduMap" //百度地图的包名
    const val GAODE_PKG = "com.autonavi.minimap" //高德地图的包名
    const val TENCENT_PKG = "com.tencent.map" //腾讯地图的包名

    //参考高德地图app路径规划: https://lbs.amap.com/api/amap-mobile/guide/android/route
    fun openGaoDe(context: BaseActivity, latitude: Double, longitude: Double, endName: String) {
        //这个是直接导航的
//        String uriStr = "androidamap://navi?sourceApplication=tuanyoubao&lat=" + latitude + "&lon=" + longitude + "&dev=0&style=2";
        //这个是进行路径规划的
        val uriStr = "amapuri://route/plan/?sname=我的位置&dlat=" + latitude + "&dlon=" +
                longitude + "&dname=" + endName + "&dev=0&t=0&sourceApplication=小象加油"
        val intent = Intent("android.intent.action.VIEW", Uri.parse(uriStr))
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setPackage("com.autonavi.minimap")
        startMapActivityForIntent(context, intent)
    }

    //参考百度地图app路径导航: http://lbsyun.baidu.com/index.php?title=uri/api/android
    fun openBaidu(context: BaseActivity, latitude: Double, longitude: Double, endName: String) {
        val i1 = Intent()
        //这是直接导航的
//        String uriStr = "baidumap://map/navi?location=" + latitude + "," + longitude + "&src=andr.czb.tuanyoubao";
        //这个是进行路径规划的
        val uriStr = "baidumap://map/direction?destination=name:" + endName + "|latlng:" +
                latitude + "," + longitude + "&coord_type=gcj02&src=andr.xxjy.xiaoxiangjiayou"
        i1.setData(Uri.parse(uriStr))
        startMapActivityForIntent(context, i1)
    }

    //参考腾讯地图调用app路径规划: http://lbs.qq.com/uri_v1/guide-mobile-navAndRoute.html
    fun openTencent(context: BaseActivity, latitude: Double, longitude: Double, endName: String) {
        val uriStr = "qqmap://map/routeplan?type=drive&from=我的位置&to=" + endName + "&tocoord=" +
                latitude + "," + longitude + "&referer=com.xxjy.jyyh"
        val i1 = Intent()
        i1.setData(Uri.parse(uriStr))
        startMapActivityForIntent(context, i1)
    }

    //参考高德地图调用h5路径规划: https://lbs.amap.com/api/uri-api/guide/travel/route
    fun openGaoDeWeb(context: BaseActivity, latitude: Double, longitude: Double, endName: String) {
        val uriStr = "https://uri.amap.com/navigation?to=" + longitude + "," + latitude + "," +
                endName + "&mode=car&src=tuanyoubao&callnative=0"
        val uri = Uri.parse(uriStr)
        val i1 = Intent(Intent.ACTION_VIEW, uri)
        startMapActivityForIntent(context, i1)
    }

    //参考腾讯地图调用h5路径规划: http://lbs.qq.com/uri_v1/guide-route.html
    fun openTencentWeb(
        context: BaseActivity,
        latitude: Double,
        longitude: Double,
        endName: String
    ) {
        val uriStr =
            "https://apis.map.qq.com/uri/v1/routeplan?type=drive&to=" + endName + "&tocoord=" +
                    latitude + "," + longitude + "&policy=0&referer=com.xxjy.jyyh"
        val uri = Uri.parse(uriStr)
        val i1 = Intent(Intent.ACTION_VIEW, uri)
        startMapActivityForIntent(context, i1)
    }

    /**
     * 根据形成的intent统一进行跳转处理
     *
     * @param context
     * @param intent
     */
    private fun startMapActivityForIntent(context: BaseActivity, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            LogUtils.e(e.message)
            context.showToastError("打开软件失败,请您自行打开您的地图软件进行导航或者选择其他地图进行导航")
        }
    }

    /**
     * 检测地图应用是否安装
     *
     * @param packagename
     * @return
     */
    fun checkMapAppsIsExist(packagename: String?): Boolean {
        return AppUtils.isAppInstalled(packagename)
    }

    /**
     * 检查手机是否有默认导航功能
     *
     * @return
     */
    val isPhoneHasMapNavigation: Boolean
        get() = isPhoneHasMapBaiDu || isPhoneHasMapGaoDe || isPhoneHasMapTencent

    /**
     * 检查手机是否有高德地图
     *
     * @return
     */
    val isPhoneHasMapGaoDe: Boolean
        get() = checkMapAppsIsExist(GAODE_PKG)

    /**
     * 检查手机是否有百度地图
     *
     * @return
     */
    val isPhoneHasMapBaiDu: Boolean
        get() = checkMapAppsIsExist(BAIDU_PKG)

    /**
     * 检查手机是否有腾讯地图
     *
     * @return
     */
    val isPhoneHasMapTencent: Boolean
        get() = checkMapAppsIsExist(TENCENT_PKG)
}