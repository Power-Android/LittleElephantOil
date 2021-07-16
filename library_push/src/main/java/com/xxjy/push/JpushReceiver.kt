package com.xxjy.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import cn.jpush.android.api.JPushInterface
import org.json.JSONException
import org.json.JSONObject

/**
 * 自定义接收器
 *
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JpushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle: Bundle? = intent.getExtras() //cn.jpush.android.intent.NOTIFICATION_OPENED
            Log.i(
                TAG,
                "intent.getAction()--->" + intent.getAction() + ", extras: " + printBundle(bundle!!)
            )
            if (JPushInterface.ACTION_REGISTRATION_ID == intent.getAction()) {
                val regId: String? = bundle?.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                Log.i(TAG, "[MyReceiver] 接收Registration Id : $regId")
                //send the Registration Id to your server...
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.getAction()) {
                Log.i(
                    TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)
                )
                //                processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.getAction()) {
                Log.i(TAG, "[MyReceiver] 接收到推送下来的通知")
                val notifactionId: Int = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                Log.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.getAction()) {
                Log.i(TAG, "[MyReceiver] 用户点击打开了通知---->")
                val extraExtra: String? = bundle?.getString(JPushInterface.EXTRA_EXTRA)
                Log.i(TAG, "extraExtra内容为: $extraExtra")
                if (TextUtils.isEmpty(extraExtra)) {
                    lunchAppIfNotExist(context)
                } else {
                    try {
                        // TODO: 2021/7/15 跳转逻辑 
//                        val (link) = GsonTool.parseJson(extraExtra, JPushExtraBean::class.java)
//                        if (!TextUtils.isEmpty(link)) {
//                            if (AppUtils.isAppForeground() && ActivityUtils.getTopActivity() != null) {
//                                val topActivity: Activity = ActivityUtils.getTopActivity()
//                                if (topActivity is BaseActivity) {
//                                    val baseActivity: BaseActivity = topActivity as BaseActivity
//                                    NaviActivityInfo.disPathIntentFromUrl(baseActivity, link)
//                                } else {
//                                    lunchAppIfNotExist(context)
//                                }
//                            } else {
//                                if (ActivityUtils.isActivityExistsInStack(MainActivity::class.java)) {
//                                    ActivityUtils.finishToActivity(MainActivity::class.java, false)
//                                    val i = Intent(context, MainActivity::class.java) //自定义打开的界面
//                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                    i.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, link)
//                                    ActivityUtils.startActivity(i)
//                                } else {
////                                    Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
////                                    i.putExtra(WelcomeActivity.TYPE_ACT_LINK, link);
////                                    ActivityUtils.startActivity(i);
//                                }
//                            }
//                        } else {
//                            lunchAppIfNotExist(context)
//                        }
                    } catch (e: Exception) {
                        lunchAppIfNotExist(context)
                    }
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.getAction()) {
                Log.i(
                    TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)
                )
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.getAction()) {
                val connected: Boolean =
                    intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                Log.i(
                    TAG,
                    "[MyReceiver]" + intent.getAction() + " connected state change to " + connected
                )
            } else if (JPushInterface.ACTION_REGISTRATION_ID == intent.getAction()) {
                Log.i(
                    TAG,
                    "[MyReceiver]" + intent.getAction() + " connected state change to "
                )
            } else {
                Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction())
            }
        } catch (e: Exception) {
            Log.i(TAG, "异常------------》" + intent.getAction())
        }
    }

    private fun lunchAppIfNotExist(context: Context) {
//        try {
//            Tool.isProcessRunning(context);
//            Intent i = new Intent(context, MessageCenterActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        } catch (Exception e) {
//            Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra(WelcomeActivity.TYPE_ACT_LINK, "msg");
//            MainActivity.stateFragment = Tool.LOGIN_TO_MY;
//            context.startActivity(i);
//        }
//        if (ActivityUtils.isActivityAlive(context)) {
            // TODO: 2021/7/15 跳转页面 
//            val i = Intent(context, MainActivity::class.java) //自定义打开的界面
//            i.putExtra("startFrom", 1)
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(i)
//        } else {
//            Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
//            i.putExtra("startFrom", 1);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
    }

    companion object {
        private const val TAG = "JPush"

        // 打印所有的 intent extra 数据
        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                    sb.append(
                        """
    
    key:$key, value:${bundle.getInt(key)}
    """.trimIndent()
                    )
                } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                    sb.append(
                        """
    
    key:$key, value:${bundle.getBoolean(key)}
    """.trimIndent()
                    )
                } else if (key == JPushInterface.EXTRA_EXTRA) {
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Log.i(TAG, "This message has no Extra data")
                        continue
                    }
                    try {
                        val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                        val it: Iterator<String> = json.keys()
                        while (it.hasNext()) {
                            val myKey = it.next()
                            sb.append(
                                """
key:$key, value: [$myKey - ${json.optString(myKey)}]"""
                            )
                        }
                    } catch (e: JSONException) {
                        Log.i(TAG, "Get message extra JSON error!")
                    }
                } else {
                    sb.append(
                        """
    
    key:$key, value:${bundle.getString(key)}
    """.trimIndent()
                    )
                }
            }
            return sb.toString()
        } //
        //    //send msg to MainActivity
        //    private void processCustomMessage(Context context, Bundle bundle) {
        ////        if (MainActivity.isForeground) {
        ////            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        ////            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        ////            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        ////            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        ////            if (!ExampleUtil.isEmpty(extras)) {
        ////                try {
        ////                    JSONObject extraJson = new JSONObject(extras);
        ////                    if (extraJson.length() > 0) {
        ////                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        ////                    }
        ////                } catch (JSONException e) {
        ////
        ////                }
        ////
        ////            }
        ////            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        ////        }
        //    }
    }
}