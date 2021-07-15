package com.xxjy.push

import android.content.Context
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.service.JPushMessageReceiver

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2020/5/29.
 */
class MyJPushMessageReceiver : JPushMessageReceiver() {
    override fun onMessage(context: Context, customMessage: CustomMessage) {
        super.onMessage(context, customMessage)
    }
}