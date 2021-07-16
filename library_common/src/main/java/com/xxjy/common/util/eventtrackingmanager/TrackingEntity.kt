package com.xxjy.common.util.eventtrackingmanager

import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.io.Serializable
import javax.inject.Inject

/**
 * @author power
 * @date 5/6/21 4:56 PM
 * @project ElephantOil
 * @description:
 */
data class TrackingEntity(

    @SerializedName("requestUriCaPlatform")
    var requestUriCaPlatform: String = "",   //是，标识客户端平台信息 0：小程序；1：安卓 2：OS: 3：H5

    @SerializedName("requestUriCity")
    var requestUriCity: String = "", //是，用户定位城市

    @SerializedName("requestUriSid")
    var requestUriSid: String = "", //是，标识APP每次的启动和关闭 每次启动生成唯一不重复的sid

    @SerializedName("requestUriStartupFrom")
    var requestUriStartupFrom: String = "", //是，标识启动APP的方式 0：应用ICON 1：Push消息 2：页面外链唤起（运营页面、微信 3：第三方APP通过URD调起

    @SerializedName("requestUriIsAbtest")
    var requestUriIsAbtest: String = "", //是，是否abtest 0：否  1：是

    @SerializedName("requestUriAbtestId")
    var requestUriAbtestId: String = "", //是，实验id

    @SerializedName("requestUriAbtestName")
    var requestUriAbtestName: String = "",//是，实验名称 ab有关暂时传null

    @SerializedName("pvId")
    var pvId: String = "", //是，每次进入页面生成一个自增且唯一的pvId

    @SerializedName("pageId")
    var pageId: String = "", //是，页面的唯一id，用来区分不同的页面

    @SerializedName("path")
    var path: String = "", //是，路径

    @SerializedName("lon")
    var lon: String = "", //是，经度

    @SerializedName("lat")
    var lat: String = "", //是，纬度

    @SerializedName("createTime")
    var createTime: String = "", //是，数据生成时间

    @SerializedName("requestUriToken")
    var requestUriToken: String = "", //是，用户token

    @SerializedName("requestUriIdfa")
    var requestUriIdfa: String = "", //否，用户设备信息，设备唯一标识

    @SerializedName("requestUriCaSource")
    var requestUriCaSource: String = "", //否，标识客户端渠道信息,上传格式为：渠道_版本号,例：qqstore01_v2.4.1

    @SerializedName("requestUriDeviceMac")
    var requestUriDeviceMac: String = "", //否，标识手机设备的MAC信息 需要加密上传

    @SerializedName("requestUriDeviceImei")
    var requestUriDeviceImei: String = "", //否，标识手机设备的imei 无法获取的上传空值“-”。需要加密

    @SerializedName("requestUriDeviceManufacturer")
    var requestUriDeviceManufacturer: String = "", //否，标识手机设备制造商信息 需要加密上传

    @SerializedName("requestUriDeviceModel")
    var requestUriDeviceModel: String = "", //否，标识手机设备的型号信息 需要加密上传

    @SerializedName("requestUriUa")
    var requestUriUa: String = "", //否，用户代理

    @SerializedName("requestUriSystemVersion")
    var requestUriSystemVersion: String = "", //否，系统版本

    @SerializedName("pageParam")
    var pageParam: String = "", //否，页面参数

    @SerializedName("fromPageId")
    var fromPageId: String = "", //否，上级页面id

    @SerializedName("fromPageParam")
    var fromPageParam: String = "", //否，上级页面参数

    @SerializedName("firstUtmSource")
    var firstUtmSource: String = "", //否，一级来源    建议增加，预留字段

    @SerializedName("secondUtmSource")
    var secondUtmSource: String = "", //否，二级来源 建议增加，预留字段

    @SerializedName("thirdUtmSource")
    var thirdUtmSource: String = "", //否，三级来源 建议增加，预留字段

)