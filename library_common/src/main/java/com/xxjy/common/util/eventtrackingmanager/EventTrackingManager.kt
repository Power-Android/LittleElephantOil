package com.xxjy.common.util.eventtrackingmanager

import android.text.TextUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.TimeUtils
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.UserConstants
import java.util.*

/**
 * @author power
 * @date 5/6/21 4:44 PM
 * @project ElephantOil
 * @description:
 */
class EventTrackingManager private constructor() {

    lateinit var mTrackingEntity: TrackingEntity

    /**
     * @param pvId 每次进入页面生成一个自增且唯一的pvId
     * @param pageId 页面的唯一id，用来区分不同的页面
     * @param path 路径
     */
    //    public void tracking(Context context, BaseActivity activity, String pvId, String pageId, String path, String pageParam){
    //        tracking( activity, pvId, pageId, path, pageParam, null, null);
    //    }
    /**
     * @param pageId 页面的唯一id，用来区分不同的页面
     */
    fun tracking(activity: BaseActivity?, pageId: String?, pageParam: String?) {
        tracking(
            activity,
            java.lang.String.valueOf(++Constants.PV_ID),
            pageId,
            "",
            pageParam,
            null,
            null
        )
    }

    fun trackingEvent(activity: BaseActivity?, pageId: String?, pageParam: String?) {
        tracking(
            activity,
            java.lang.String.valueOf(++Constants.PV_ID),
            pageId,
            "",
            pageParam,
            null,
            null
        )
    }

    /**
     * @param pvId
     * @param pageId
     * @param path
     * @param fromPageId    上级页面id
     * @param fromPageParam 上级页面参数
     */
    fun tracking(
        activity: BaseActivity?, pvId: String?, pageId: String?, path: String?, pageParam: String?,
        fromPageId: String?, fromPageParam: String?
    ) {
        setParams(activity)
        if (!TextUtils.isEmpty(pvId)) {
            mTrackingEntity.pvId = pvId!!
        }
        if (!TextUtils.isEmpty(pageId)) {
            mTrackingEntity.pageId = pageId!!
        }
        if (!TextUtils.isEmpty(path)) {
            mTrackingEntity.path = path!!
        }
        if (!TextUtils.isEmpty(pageParam)) {
            mTrackingEntity.pageParam = pageParam!!
        }
        if (!TextUtils.isEmpty(fromPageId)) {
            mTrackingEntity.fromPageId = fromPageId!!
        }
        if (!TextUtils.isEmpty(fromPageParam)) {
            mTrackingEntity.fromPageParam = fromPageParam!!
        }
        request(activity)
    }

    private fun setParams(activity: BaseActivity?) {
        mTrackingEntity = TrackingEntity()
        mTrackingEntity.requestUriCaPlatform = "1"
        mTrackingEntity.requestUriCity = UserConstants.ad_code
        mTrackingEntity.requestUriSid = TimeUtils.getNowMills().toString()
        // 0：应用ICON 1：Push消息 2：页面外链唤起（运营页面、微信） 3：第三方APP
        mTrackingEntity.requestUriStartupFrom = UserConstants.startFrom
        mTrackingEntity.lon = UserConstants.longitude
        mTrackingEntity.lat = UserConstants.latitude
        mTrackingEntity.createTime = TimeUtils.getNowString()
        mTrackingEntity.requestUriToken = UserConstants.token
        mTrackingEntity.requestUriIdfa = DeviceUtils.getUniqueDeviceId()
        //TODO late
//        mTrackingEntity.requestUriCaSource = UMengManager.getChannelName(activity)
        mTrackingEntity.requestUriDeviceMac = DeviceUtils.getMacAddress()
        mTrackingEntity.requestUriDeviceManufacturer = DeviceUtils.getManufacturer()
        mTrackingEntity.requestUriDeviceModel = DeviceUtils.getModel()
        mTrackingEntity.requestUriSystemVersion = DeviceUtils.getSDKVersionName()
    }

    fun getParams(activity: BaseActivity?, pvId: String): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map["requestUriCaPlatform"] = "1"
        map["requestUriCity"] = UserConstants.ad_code
        map["requestUriSid"] = TimeUtils.getNowMills().toString()
        map["requestUriStartupFrom"] = UserConstants.startFrom
        map["lon"] = UserConstants.longitude
        map["lat"] = UserConstants.latitude
        map["createTime"] = TimeUtils.getNowString()
        map["requestUriToken"] = UserConstants.token
        map["requestUriIdfa"] = DeviceUtils.getUniqueDeviceId()
        //TODO late
//        map["requestUriCaSource"] = UMengManager.getChannelName(activity)
        map["requestUriDeviceMac"] = DeviceUtils.getMacAddress()
        map["requestUriDeviceManufacturer"] = DeviceUtils.getManufacturer()
        map["requestUriDeviceModel"] = DeviceUtils.getModel()
        map["requestUriSystemVersion"] = DeviceUtils.getSDKVersionName()
        if (!TextUtils.isEmpty(pvId)) {
            map["pvId"] = pvId
        }
        if (!TextUtils.isEmpty(TrackingConstant.OIL_MAIN_TYPE)) {
            map["fromPageParam"] = TrackingConstant.OIL_MAIN_TYPE
        }
        return map
    }

    private fun request(activity: BaseActivity?) {
        //http://39.106.218.38:11000
//        RxHttp.postJson(if (Constants.URL_IS_DEBUG) "http://39.106.218.38:11000/v1/clickData/add" else "https://click.xiaoxiangjiayou.com/v1/clickData/add")
//            .add("requestUriCaPlatform", mTrackingEntity.requestUriCaPlatform)
//            .add("requestUriCity", mTrackingEntity.requestUriCity)
//            .add("requestUriSid", mTrackingEntity.requestUriSid)
//            .add("requestUriStartupFrom", mTrackingEntity.requestUriStartupFrom)
//            .add("lon", mTrackingEntity.lon)
//            .add("lat", mTrackingEntity.lat)
//            .add("createTime", mTrackingEntity.createTime)
//            .add("requestUriToken", mTrackingEntity.requestUriToken)
//            .add("requestUriIdfa", mTrackingEntity.requestUriIdfa)
//            .add("requestUriCaSource", mTrackingEntity.requestUriCaSource)
//            .add("requestUriDeviceMac", mTrackingEntity.requestUriDeviceMac)
//            .add("requestUriDeviceManufacturer", mTrackingEntity.requestUriDeviceManufacturer)
//            .add("requestUriDeviceModel", mTrackingEntity.requestUriDeviceModel)
//            .add("requestUriSystemVersion", mTrackingEntity.requestUriSystemVersion)
//            .add(
//                "pvId", mTrackingEntity.pvId,
//                !TextUtils.isEmpty(mTrackingEntity.pvId)
//            )
//            .add(
//                "pageId", mTrackingEntity.pageId,
//                !TextUtils.isEmpty(mTrackingEntity.pageId)
//            )
//            .add(
//                "path", mTrackingEntity.path,
//                !TextUtils.isEmpty(mTrackingEntity.path)
//            )
//            .add(
//                "pageParam", mTrackingEntity.pageParam,
//                !TextUtils.isEmpty(mTrackingEntity.pageParam)
//            )
//            .add(
//                "fromPageId", mTrackingEntity.fromPageId,
//                !TextUtils.isEmpty(mTrackingEntity.fromPageId)
//            )
//            .add(
//                "fromPageParam", mTrackingEntity.fromPageParam,
//                !TextUtils.isEmpty(mTrackingEntity.fromPageParam)
//            )
//            .asString()
//            .to(RxLife.toMain(activity))
//            .subscribe(Consumer<String?> { })
    }

    companion object {
        private lateinit var mInstance: EventTrackingManager
        val instance: EventTrackingManager
            get() {
                if (mInstance == null) {
                    synchronized(EventTrackingManager::class.java) {
                        if (mInstance == null) {
                            mInstance = EventTrackingManager()
                        }
                    }
                }
                return mInstance
            }
    }
}