package com.xxjy.jyyh

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import androidx.multidex.MultiDex
import com.taobao.sophix.PatchStatus
import com.taobao.sophix.SophixApplication
import com.taobao.sophix.SophixEntry
import com.taobao.sophix.SophixManager
import com.xxjy.common.constants.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SophixStubApplication : SophixApplication() {
    private val TAG = "SophixStubApplication"

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(App::class)
    internal class RealApplicationStub

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this)
        initSophix()
    }

    private fun initSophix() {
        var appVersion = "0.0.0"
        try {
            appVersion = this.getPackageManager()
                .getPackageInfo(this.getPackageName(), 0).versionName
        } catch (e: Exception) {
        }
        val instance: SophixManager = SophixManager.getInstance()
        instance.setContext(this)
            .setAppVersion(appVersion)
            .setSecretMetaData(null, null, null)
            .setEnableDebug(false)
            .setEnableFullLog()
            .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
                if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                    Log.i(TAG, "sophix load patch success!")
                } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                    // 如果需要在后台重启，建议此处用SharePreference保存状态。
                    Log.i(TAG, "sophix preload patch success. restart app to make effect.")
                }
            }.initialize()
    }

    override fun onCreate() {
        super.onCreate()
        if (!Constants.IS_DEBUG) {
            SophixManager.getInstance().queryAndLoadNewPatch()
        }
    }
}