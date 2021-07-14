 package com.xxjy.navigation

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2017/9/15.
 * 高德地图的 帮助类
 */
class MapLocationHelper private constructor() {
    /**
     * 声明mlocationClient对象
     */
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null

    /**
     * 定位结果
     */
    private var mLocationResult: LocationResult? = null

    /**
     * 获取定位坐标
     */
    private fun initLocationClient(context: Context) {
        mLocationClient = AMapLocationClient(context)
        //设置定位监听
        mLocationClient?.setLocationListener(locationListener)

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
//        mLocationClient.startLocation();
    }

    //获取一次定位结果：
    //获取最近3s内精度最高的一次定位结果：
    //设置是否返回地址信息（默认返回地址信息）
    //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
    //可选，设置是否使用缓存定位，默认为true
    //主动刷新设备wifi模块，获取到最新鲜的wifi列表
//        mOption.setWifiActiveScan(true);
    private val signInOption: AMapLocationClientOption
        private get() {
            val mOption = AMapLocationClientOption()
            mOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn)
            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            //获取一次定位结果：
            mOption.setOnceLocation(true)
            //获取最近3s内精度最高的一次定位结果：
            mOption.setOnceLocationLatest(true)
            //设置是否返回地址信息（默认返回地址信息）
            mOption.setNeedAddress(true)
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mOption.setHttpTimeOut(20000)
            //可选，设置是否使用缓存定位，默认为true
            mOption.setLocationCacheEnable(true)
            //主动刷新设备wifi模块，获取到最新鲜的wifi列表
//        mOption.setWifiActiveScan(true);
            return mOption
        }//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
    //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
    //可选，设置定位间隔。默认为2秒
    //可选，设置是否返回逆地理地址信息。默认是true
    //可选，设置是否只定位一次,默认为false
    //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用 ,获取最近3s内精度最高的一次定位结果
    //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
    //可选，设置是否使用传感器。默认是false
    //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
    //可选，设置是否使用缓存定位，默认为true
    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private val defaultOption: AMapLocationClientOption
        private get() {
            val mOption = AMapLocationClientOption()
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mOption.setGpsFirst(false)
            //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mOption.setHttpTimeOut(20000)
            //可选，设置定位间隔。默认为2秒
            mOption.setInterval(2000)
            //可选，设置是否返回逆地理地址信息。默认是true
            mOption.setNeedAddress(true)
            //可选，设置是否只定位一次,默认为false
            mOption.setOnceLocation(true)
            //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用 ,获取最近3s内精度最高的一次定位结果
            mOption.setOnceLocationLatest(true)
            //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)
            //可选，设置是否使用传感器。默认是false
            mOption.setSensorEnable(false)
            //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mOption.setWifiScan(true)
            //可选，设置是否使用缓存定位，默认为true
            mOption.setLocationCacheEnable(true)
            return mOption
        }

    /**
     * 定位监听
     */
    var locationListener: AMapLocationListener = object : AMapLocationListener {
        override fun onLocationChanged(location: AMapLocation) {
            if (null != location) {
                val sb = StringBuffer()
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    mLastRefreshTime = System.currentTimeMillis()
                    locationLatitude = location.getLatitude()
                    locationLongitude = location.getLongitude()
                    cityCode = location.getCityCode()
                    adCode = location.adCode
                    city = location.getCity()
                    Log.i("定位",cityCode + "---" + location.getLatitude() + "---" + location.getLongitude())
                    aMapLocation = location
                    isHasLocationPermission = true
                    // TODO: 2021/7/7
//                    UserConstants.setLongitude(location.getLongitude().toString())
//                    UserConstants.setLatitude(location.getLatitude().toString())
                    if (mLocationResult != null) {
                        mLocationResult!!.locationSuccess(location)
                    }
                    sb.append(
                        """
    定位成功
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    定位类型: ${location.getLocationType()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    经    度    : ${location.getLongitude()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    纬    度    : ${location.getLatitude()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    精    度    : ${location.getAccuracy()}米
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    提供者    : ${location.getProvider()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    速    度    : ${location.getSpeed()}米/秒
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    角    度    : ${location.getBearing()}
    
    """.trimIndent()
                    )
                    // 获取当前提供定位服务的卫星个数
                    sb.append(
                        """
    星    数    : ${location.getSatellites()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    国    家    : ${location.getCountry()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    省            : ${location.getProvince()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    市            : ${location.getCity()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    城市编码 : ${location.getCityCode()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    区            : ${location.getDistrict()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    区域 码   : ${location.getAdCode()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    地    址    : ${location.getAddress()}
    
    """.trimIndent()
                    )
                    sb.append(
                        """
    兴趣点    : ${location.getPoiName()}
    
    """.trimIndent()
                    )
                    //定位完成的时间
//                    sb.append("定位时间: " + DateUtils.formatDate(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    if (location.getErrorCode() == 12) {
                        isHasLocationPermission = false
                    }
                    if (mLocationResult != null) {
                        mLocationResult!!.locationFiler()
                    }
                }
                //定位之后的回调时间
//                sb.append("回调时间: " + DateUtils.formatDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                val result = sb.toString()
//                LogUtils.e("定位", result)
                Log.d("定位",result)
            } else {
                if (mLocationResult != null) {
                    mLocationResult!!.locationFiler()
                }
            }
        }
    }

    interface LocationResult {
        fun locationSuccess(location: AMapLocation?)
        fun locationFiler()
    }

    /**
     * 获取位置
     */
    fun getLocation(context: Context,locationResult: LocationResult?) {
        mLocationResult = locationResult
        if (mLocationClient == null) {
            initLocationClient(context)
        }
        //初始化定位参数
        if (mLocationOption == null) {
            mLocationOption = signInOption
        }
        //设置定位参数
        mLocationClient?.setLocationOption(mLocationOption)
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient?.stopLocation()
        mLocationClient?.startLocation()
    }

    /**
     * 内部停止定位
     */
    private fun stopInnerLocation() {
        mLocationResult = null
            mLocationClient?.stopLocation()
    }

    /**
     * 内部不在定位了
     */
    private fun onInnerDestory() {
            mLocationClient?.unRegisterLocationListener(locationListener)
            mLocationClient?.onDestroy()
            mLocationClient = null
    }

    companion object {
        /**
         * 定位的有效时间
         */
        private const val LOCATION_VALID_TIME = (60 * 1000).toLong()

        /**
         * 获取纬度
         *
         * @return
         */
        //            private static final long LOCATION_VALID_TIME = 1000;
        var locationLatitude = 0.0
            private set

        /**
         * 获取经度
         *
         * @return
         */
        var locationLongitude = 0.0 //标记当前的经纬度
            private set

        private var mLastRefreshTime: Long = 0 //标记上次刷新的时间
        private var aMapLocation //保存当前的location
                : AMapLocation? = null
        var isHasLocationPermission = true //标记当前是否有地址的使用权限, 根据实际获取到的位置来判断
            private set

        /**
         * 获取单例对象
         *
         * @return
         */
        // 单例对象
        var instance: MapLocationHelper? = null
            get() {
                if (field == null) {
                    synchronized(MapLocationHelper::class.java) {
                        if (field == null) {
                            field = MapLocationHelper()
                        }
                    }
                }
                return field
            }
            private set
        var cityCode: String? = null
        var adCode: String? = null
        var city: String? = null

        /**
         * 刷新位置
         */
        fun refreshLocation(context: Context) {
            instance!!.getLocation(context,null)
        }

        /**
         * 判断当前的 location 位置是否有效
         * 如果无效会刷新位置
         *
         * @return  true 当前位置有效, false代表当前位置无效
         */
        val isLocationValid: Boolean
            get() = System.currentTimeMillis() - mLastRefreshTime < LOCATION_VALID_TIME

        /**
         * 获取当前保存 的 AMapLocation 对象
         *
         * @return
         */
        val mapLocation: AMapLocation?
            get() = aMapLocation

        /**
         * 停止定位
         */
        fun stopLocation() {
            instance!!.stopInnerLocation()
        }

        /**
         * 不再定位了
         */
        fun onDestory() {
            instance!!.onInnerDestory()
        }
    }
}