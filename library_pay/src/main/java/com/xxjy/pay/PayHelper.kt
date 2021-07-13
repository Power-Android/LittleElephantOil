package com.xxjy.pay

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.unionpay.UPPayAssistEx
import java.lang.ref.WeakReference

class PayHelper {
    //微信配置
     val WX_APP_ID = "wx3704434db8357ec1"
     val WX_APP_SCRIPT = "787d5dcefab80f6bca272800e9bad139" //ab730ab00dd73986593da2ce6514ffe8     6b4edd26960e017c050f940210a99723
    var msgApi: IWXAPI? = null
    fun WexPay(context: Context?,
               appId:String,
               partnerId:String,
               prepayId:String,
               nonceStr:String,
               timeStamp:String,
               packageValue:String,
               sign:String) {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(context, null)
            msgApi?.registerApp(WX_APP_ID) // 将该app注册到微信
        }
        val req = PayReq()
        if (!(msgApi?.isWXAppInstalled())!!) {
            Toast.makeText(context, "手机中没有安装微信客户端!", Toast.LENGTH_SHORT).show()
            return
        }
            req.appId = appId
            req.partnerId = partnerId
            req.prepayId = prepayId
            req.nonceStr = nonceStr
            req.timeStamp = timeStamp
            req.packageValue = packageValue
            req.sign = sign
            msgApi?.sendReq(req)
    }

    fun unionPay(context: Context?, payNo: String?) {
        UPPayAssistEx.startPay(context, null, null, payNo, "00")
    }

    fun AliPay(activity: Activity?, orderInfo: String?) {
        val myHandler = MyHandler(activity)
        val alipay = PayTask(activity)
        val payRunnable = Runnable {
            val result: Map<String, String> = alipay.payV2(orderInfo, true)
            Log.i("msp", result.toString())
            val msg = Message()
            msg.what = 0
            msg.obj = result
            myHandler.sendMessage(msg)
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    private inner class MyHandler(activity: Activity?) : Handler() {
        var mActivity: WeakReference<Activity?>
        override fun handleMessage(msg: Message) {
            // 处理消息...
            when (msg.what) {
                0 -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.result // 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        if (mIPayListener != null) {
//                            mIPayListener.onSuccess();
//                        }
                        PayListenerUtils.instance?.addSuccess()
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        PayListenerUtils.instance?.addCancel()
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        if (mIPayListener != null) {
//                            mIPayListener.onFail();
//                        }
                        PayListenerUtils.instance?.addFail()
                    }
                }
            }
        }

        init {
            mActivity = WeakReference<Activity?>(activity)
        }
    } //    public  IPayListener mIPayListener;

    //
    //    public void setIPayListener(IPayListener mIPayListener) {
    //        this.mIPayListener = mIPayListener;
    //    }
    //    public interface IPayListener {
    //        void onSuccess();
    //
    //        void onFail();
    //    }
    companion object {
        private var mPayHelper: PayHelper? = null
        val instance: PayHelper?
            get() {
                if (mPayHelper == null) {
                    mPayHelper = PayHelper()
                }
                return mPayHelper
            }
    }
}