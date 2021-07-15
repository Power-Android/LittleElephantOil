package com.xxjy.web.jscalljava.jscallback

/**
 * 车主邦
 * ---------------------------
 *
 *
 * Created by zhaozh on 2017/12/12.
 */
interface OnJsCallListener {
    fun onJsCallListener(callType: Int, content: String?)

    companion object {
        const val CALL_TYPE_SHARE = 1
    }
}