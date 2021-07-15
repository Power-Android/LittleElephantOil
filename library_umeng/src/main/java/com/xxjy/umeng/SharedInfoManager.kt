package com.xxjy.umeng

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.umeng.socialize.shareboard.ShareBoardConfig

object SharedInfoManager {

    private fun sharedInfo(
        activity: AppCompatActivity, checkType: SHARE_MEDIA, imgUrl: String,
        shareLink: String, title: String, description: String,
        listener: OnSharedResultListener?
    ) {
        val image = UMImage(activity, imgUrl) //网络图片
        val web = UMWeb(shareLink)
        web.setTitle(title) //标题
        web.setDescription(description)
        web.setThumb(image)
        val config = ShareBoardConfig()
        config.setIndicatorVisibility(false)
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE)
        ShareAction(activity).withMedia(web)
            .setPlatform(checkType)
            .setCallback(object : UMShareListener {
                override fun onStart(platform: SHARE_MEDIA) {
                    if (listener != null && listener is OnSharedAllResultListener) {
                        listener.onStart(platform)
                    }
                }

                override fun onResult(platform: SHARE_MEDIA) {
                    listener?.onSharedSuccess(platform)
                }

                override fun onError(platform: SHARE_MEDIA, t: Throwable) {
                    listener?.onSharedError(platform, t)
                }

                override fun onCancel(platform: SHARE_MEDIA) {
                    if (listener != null && listener is OnSharedAllResultListener) {
                        listener.onCancel(platform)
                    }
                }
            }).share()
    }


    /**
     * 获取一个默认的监听,会通过baseactivity来进行分享结果的toast显示;
     *
     * @param activity
     * @return
     */
    fun getDefaultListener(activity: AppCompatActivity): OnSharedResultListener {
        return object : OnSharedResultListener {
            override fun onSharedSuccess(platform: SHARE_MEDIA?) {
                Toast.makeText(activity,"分享成功",Toast.LENGTH_SHORT).show()
            }

            override fun onSharedError(platform: SHARE_MEDIA?, t: Throwable) {
                Toast.makeText(activity,"分享失败: " + t.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface OnSharedResultListener {
        fun onSharedSuccess(platform: SHARE_MEDIA?)
        fun onSharedError(platform: SHARE_MEDIA?, t: Throwable)
    }

    interface OnSharedAllResultListener : OnSharedResultListener {
        fun onStart(platform: SHARE_MEDIA?)
        fun onCancel(platform: SHARE_MEDIA?)
    }
}