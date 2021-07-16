package com.xxjy.web.jscalljava

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ThreadUtils
import com.umeng.socialize.bean.SHARE_MEDIA
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.constants.Constants
import com.xxjy.common.constants.SPConstants
import com.xxjy.common.constants.UserConstants
import com.xxjy.common.http.HttpManager
import com.xxjy.common.util.ImageUtils
import com.xxjy.common.util.MapIntentUtils
import com.xxjy.common.util.WXSdkManager
import com.xxjy.jyyh.ui.web.WebViewActivity
import com.xxjy.navigation.MapLocationHelper
import com.xxjy.umeng.UMengLoginWx
import com.xxjy.web.jscalljava.jsbean.OrderBean
import com.xxjy.web.jscalljava.jscallback.OnJsCallListener
import org.json.JSONObject

/**
 * 该类提供了 js 调用的 接口 , 可以增加方法的方式来进行跳转 , h5 调用端的代码为 :  window.benXiang.getAppInfo() 方法名字
 */
class JsOperation(activity: BaseActivity?) : JsOperationMethods {
    private var mActivity: BaseActivity?
    private var mListener: OnJsCallListener? = null
    fun setOnJsCallListener(listener: OnJsCallListener?) {
        mListener = listener
    }

    //获取appinfo
    @JavascriptInterface
    override fun getAppInfo(): String {
        val finalParams: MutableMap<String, String> = HttpManager.getCommonParams(null)
        finalParams[SPConstants.APP_TOKEN] = UserConstants.token
        val mapLocation: AMapLocation? = MapLocationHelper.aMapLocation
        if (mapLocation != null) {
            finalParams["cityName"] = mapLocation.city
        }
        val params = JSONObject(finalParams.toMap())
        Log.e("getAppInfo", params.toString())
        return params.toString()
    }

    @JavascriptInterface
    override fun getEventTracking(): String {
//            val params: Map<String, String> = EventTrackingManager.getInstance()
//                .getParams(mActivity, java.lang.String.valueOf(++Constants.PV_ID))
//            val jsonObject = JSONObject(params)
//            Log.e("getEventTracking", jsonObject.toString())
//            return jsonObject.toString()
        return ""
    }

    // 调起分享
    @JavascriptInterface
    override fun startShare(shardInfo: String?) {
        LogUtils.e("获取到的数据为: $shardInfo")
        if (mListener != null) {
            mActivity?.runOnUiThread(Runnable {
                mListener?.onJsCallListener(
                    OnJsCallListener.CALL_TYPE_SHARE,
                    shardInfo
                )
            })
        }
    }

    @JavascriptInterface
    override fun showSharedIcon(shardInfo: String?) {
        mActivity?.runOnUiThread(Runnable {
            if (mActivity is WebViewActivity) {
                (mActivity as WebViewActivity).showSharedIcon(shardInfo!!)
            }
        })
    }

    @JavascriptInterface
    override fun hideSharedIcon() {
        mActivity?.runOnUiThread {
            if (mActivity is WebViewActivity) {
                (mActivity as WebViewActivity).hideSharedIcon()
            }
        }
    }

    @JavascriptInterface
    override fun showHelpIcon(phoneNumber: String?) {
        mActivity?.runOnUiThread(Runnable {
            if (mActivity is WebViewActivity) {
                if (mActivity is WebViewActivity) {
                    (mActivity as WebViewActivity).showHelpIcon(phoneNumber!!)
                }
            }
        })
    }

    @JavascriptInterface
    override fun toRechargeOneCard() {
        mActivity?.runOnUiThread(Runnable {
            //                RechargeOneCardActivity.openRechargeOneCardAct(mActivity);
        })
    }

    //拨打电话
    @JavascriptInterface
    override fun dialPhone(phoneNumber: String?) {
        mActivity?.runOnUiThread(Runnable {
            if (!TextUtils.isEmpty(phoneNumber)) {
                try {
                    val phoneUri = Uri.parse("tel:$phoneNumber")
                    val intent = Intent(Intent.ACTION_DIAL, phoneUri)
                    if (intent.resolveActivity(mActivity!!.packageManager) != null) {
                        mActivity?.startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    //跳登录
    @JavascriptInterface
    override fun toLogin() {
        mActivity?.runOnUiThread(Runnable {

            if (mActivity is WebViewActivity) {
                (mActivity as WebViewActivity).isShouldLoadUrl=true
            }
//            UiUtils.toLoginActivity(mActivity, Constants.LOGIN_FINISH)
        })
    }

    @JavascriptInterface
    override fun toWechatPay(url: String?) {
        LogUtils.e("支付参数：", url)
        mActivity?.runOnUiThread(Runnable {
//            WeChatWebPayActivity.openWebPayAct(mActivity, url)
        })
    }

    @JavascriptInterface
    override fun toAppletPay(params: String?) {
        mActivity?.runOnUiThread(Runnable {
//                            WXSdkManager.newInstance().useWXLaunchMiniProgram(mActivity, params);
        })
    }

    @JavascriptInterface
    override fun toAccountDetails(checkPosition: String?) {
        mActivity?.runOnUiThread(Runnable {
            //                AccountDetailListActivity.openAccountDetailAct(mActivity, checkPosition);
        })
    }

    //跳转本地方法
    @JavascriptInterface
    override fun nativeActivity(urlInfo: String?) {
        mActivity?.runOnUiThread(Runnable {
            //                LogUtils.d("urlInfo = " + urlInfo);
//                NaviActivityInfo.disPathIntentFromUrl(mActivity, urlInfo);
        })
    }

    @JavascriptInterface
    override fun toAliPay(intentInfo: String?) {
        mActivity?.runOnUiThread(Runnable {
            if (mActivity is WebViewActivity) {
//                    LogUtils.e(intentInfo);
                val webViewActivity: WebViewActivity? = mActivity as WebViewActivity?
//                webViewActivity.startAliPay(intentInfo)
            }
        })
    }

    @JavascriptInterface
    override fun changeToolBarState(bgColor: String?) {
        mActivity?.runOnUiThread(Runnable {
            if (mActivity is WebViewActivity) {
                (mActivity as WebViewActivity).changeToolBarState(false, bgColor!!)
            }
        })
    }

    @JavascriptInterface
    override fun changeToolBarDefault() {
        mActivity?.runOnUiThread {
            if (mActivity is WebViewActivity) {
                (mActivity as WebViewActivity).changeToolBarState(true, "")
            }
        }
    }

    /**
     * 权益卡
     *
     * @param parameter
     */
    @JavascriptInterface
    override fun goWeChatBuyEquityCard(parameter: String?) {
//
        Log.e("goWeChatBuyEquityCard",parameter!!);
         var orderEntity:OrderBean=GsonUtils.fromJson(parameter, OrderBean::class.java)
        Log.e("goWeChatBuyEquityCard","orderId="+orderEntity.orderId)
        if(mActivity is WebViewActivity){
         mActivity?.runOnUiThread {
//             WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(mActivity, orderEntity.orderId);
         };
        }
    }

    @JavascriptInterface
    override fun saveImage(data: String?) {
        Log.e("saveImage", "data==>$data")
        //        ImageUtils.saveImage(mActivity,data);
        PermissionUtils.permissionGroup(PermissionConstants.STORAGE)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    ImageUtils.saveImage(mActivity!!, data!!)
                }

                override fun onDenied() {
                    ThreadUtils.runOnUiThread(Runnable {
                        Toast.makeText(
                            mActivity,
                            "权限被拒绝，无法保存",
                            Toast.LENGTH_SHORT
                        )
                    })
                }
            })
            .request()
    }

    @JavascriptInterface
    override fun shareImageToWeChat(data: String?) {
        Log.e("shareImageToWeChat", "data==>$data")
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                WXSdkManager.newInstance().shareWX(mActivity, data)
            })
        }
    }

    @JavascriptInterface
    override fun toRefuellingPage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                UiUtils.jumpToHome(mActivity, Constants.TYPE_OIL)
            })
        }
    }

    @JavascriptInterface
    override fun toHomePage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                UiUtils.jumpToHome(mActivity, Constants.TYPE_HOME)
            })
        }
    }

    @JavascriptInterface
    override fun toCarServePage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                UiUtils.jumpToHome(
//                    mActivity,
//                    Constants.TYPE_CAR_SERVE
//                )
            })
        }
    }

    @JavascriptInterface
    override fun toIntegralHomePage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                UiUtils.jumpToHome(
//                    mActivity,
//                    Constants.TYPE_INTEGRAL
//                )
            })
        }
    }

    //                    String accessToken = map.get("accessToken");
    @JavascriptInterface
    override fun getOpenId() {
        UMengLoginWx.getOpenIdForWX(mActivity, object : UMengLoginWx.UMAuthAdapter() {
           override fun onComplete(share_media: SHARE_MEDIA?, i: Int, map: Map<String?, String?>?) {
//                LogUtils.e(Gson().toJson(map))
                if (map != null && map.containsKey("openid")) {
                    val openId = map["openid"]
                    //                    String accessToken = map.get("accessToken");
                    if (mActivity is WebViewActivity) {
                        mActivity?.runOnUiThread {
                            (mActivity as WebViewActivity).setOpenId(openId!!)
                        }
                    }
                }
            }
        })
    }

    @JavascriptInterface
    override fun goCouponListPage() {
        mActivity?.runOnUiThread(Runnable {
//            mActivity.startActivity(
//                Intent(
//                    mActivity,
//                    MyCouponActivity::class.java
//                )
//            )
        })
    }

    @JavascriptInterface
    override fun toLoginByInviteFriends() {
        mActivity?.runOnUiThread(Runnable {
            if (mActivity is WebViewActivity) {
                val webViewActivity: WebViewActivity? = mActivity as WebViewActivity?
//                webViewActivity.setShouldLoadUrl(true)
            }
//            UiUtils.toLoginActivity(mActivity, Constants.LOGIN_FINISH, true)
        })
    }

    @JavascriptInterface
    override fun toMyPage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                UiUtils.jumpToHome(mActivity, Constants.TYPE_MINE)
            })
        }
    }

    @JavascriptInterface
    override fun toEquityOrderListPage() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                mActivity.startActivity(
//                    Intent(
//                        mActivity,
//                        OtherOrderListActivity::class.java
//                    ).putExtra("isIntegral", true)
//                )
            })
        }
    }

    @JavascriptInterface
    override fun showCustomerService() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                try {
//                    val customerServiceDialog = CustomerServiceDialog(mActivity)
//                    customerServiceDialog.show(mActivity.getWindow().getDecorView())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
            })
        }
    }

    @JavascriptInterface
    override fun toNavigation(longitude: String?, latitude: String?, destination: String?) {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                if (MapIntentUtils.isPhoneHasMapNavigation()) {
//                    val navigationDialog = NavigationDialog(
//                        mActivity, latitude!!.toDouble(), longitude!!.toDouble(),
//                        destination
//                    )
//                    navigationDialog.show()
//                } else {
//                    mActivity.showToastWarning("您当前未安装地图软件，请先安装")
//                }
            })
        }
    }

    @JavascriptInterface
    override fun showSearch(isShow: Boolean) {

//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showSearchView(isShow);
//                }
//            });
//
//        }
    }

    @JavascriptInterface
    override fun showToolbar(isShow: Boolean) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showToolbarView(isShow);
//                }
//            });
//
//        }
    }

    @JavascriptInterface
    override fun showPerformanceCenterView(isShow: Boolean) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showPerformanceCenterView(isShow);
//                }
//            });
//
//        }
    }

    @JavascriptInterface
    override fun goBack() {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                (mActivity as WebViewActivity?).onBackClick()
            })
        }
    }

    @JavascriptInterface
    override fun getStatusBarHeight(): Int {
//    return StatusBarUtil.getStatusBarHeight(mActivity)
        return 0
    }

    @JavascriptInterface
    override fun showImmersiveToolBar(isShow: Boolean) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showImmersiveToolBar(isShow);
//                }
//            });
//
//        }
    }

    @JavascriptInterface
    override fun showWithdrawalDetailsView(isShow: Boolean) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showWithdrawalDetailsView(isShow);
//                }
//            });
//
//        }
    }

    @JavascriptInterface
    override fun changeToolBarState(isDefault: Boolean, bgColor: String?) {
        if (mActivity is WebViewActivity) {
            mActivity?.runOnUiThread(Runnable {
//                (mActivity as WebViewActivity?).changeToolBarState(
//                    isDefault,
//                    bgColor
//                )
            })
        }
    }

    @JavascriptInterface
    override fun goWeChat() {
//        try {
//            val intent = Intent(Intent.ACTION_MAIN)
//            val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
//            intent.addCategory(Intent.CATEGORY_LAUNCHER)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.setComponent(cmp)
//            mActivity.startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//            e.printStackTrace()
//            mActivity.runOnUiThread(Runnable { Toasty.error(mActivity, "启动微信失败，请手动打开微信").show() })
//        }
    }

    @JavascriptInterface
    override fun finishActivity() {
        if (mActivity is WebViewActivity) {
            mActivity?.finish()
        }
    }

    @JavascriptInterface
    fun addCalendar(result: String?) {
        mActivity?.runOnUiThread(Runnable {
            //                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.addToCalendar(result);
//                }
        })
    }

    @JavascriptInterface
    fun cancelCalendar(result: String?) {
        mActivity?.runOnUiThread(Runnable {
            //                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.deleteFromCalendar(result);
//                }
        })
    }

    fun onDestory() {
        mActivity = null
        mListener = null
    }

    companion object {
        const val JS_USE_NAME = "littleElephant"
    }

    init {
        mActivity = activity
    }
}