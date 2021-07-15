package com.xxjy.umeng

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareConfig
import com.umeng.socialize.bean.SHARE_MEDIA

object UMengLoginWx {
    fun loginFormWx(activity: AppCompatActivity?, umAuthAdapter: UMAuthAdapter?) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthAdapter)
    }

    fun getOpenIdForWX(activity: AppCompatActivity?, umAuthAdapter: UMAuthAdapter?) {
        val config = UMShareConfig()
        config.isNeedAuthOnGetUserInfo(true)
        UMShareAPI.get(activity).setShareConfig(config)
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthAdapter)
    }

    fun deleteOauthWx(activity: AppCompatActivity?, umAuthAdapter: UMAuthAdapter?) {
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, umAuthAdapter)
    }

    abstract class UMAuthAdapter : UMAuthListener {
        override fun onStart(share_media: SHARE_MEDIA) {

        }

        override fun onError(share_media: SHARE_MEDIA, i: Int, throwable: Throwable) {

        }

        override fun onCancel(share_media: SHARE_MEDIA, i: Int) {

        }
    }
}