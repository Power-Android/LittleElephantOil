package com.xxjy.common.dialog

import android.Manifest
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PermissionUtils
import com.rxlife.coroutine.RxLifeScope
import com.xxjy.common.R
import com.xxjy.common.base.BaseActivity
import com.xxjy.common.base.BaseDialog
import com.xxjy.common.entity.VersionEntity
import com.xxjy.common.util.toastlib.Toasty.error
import kotlinx.coroutines.Dispatchers
import rxhttp.RxHttp
import rxhttp.awaitResult
import rxhttp.toDownload
import java.io.File

/**
 * ---------------------------
 * 版本更新弹窗
 */
@RequiresApi(Build.VERSION_CODES.CUPCAKE)
class VersionUpDialog(private val activity: BaseActivity, version: VersionEntity, isExit: Boolean) : BaseDialog(
    activity, Gravity.CENTER, false, false
) {

    var version: VersionEntity
    private val forceUpdate: Int
    private var sdPath = ""
    private var isDownLoadSuccess = false
    private var confirm: TextView? = null
    private var content: TextView? = null
    private var cancle: TextView? = null
    private var lineProgress: ProgressBar? = null
    private var isExit: Boolean = false
    var rxLifeScope: RxLifeScope = RxLifeScope()

    init {
        forceUpdate = version.forceUpdate
        this.version = version
        this.isExit = isExit
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        confirm = findViewById(R.id.confirm)
        cancle = findViewById(R.id.cancle)
        content = findViewById(R.id.content)
        lineProgress = findViewById(R.id.line_progress)
    }

    override val contentLayoutId: Int
        protected get() = R.layout.dialog_up_version

    private fun initData() {
        /* 设置弹出窗口特征 */
        // 设置视图     是否强制更新，0：否，1：是
        if (forceUpdate == FORCE_UPDATE_TRUE) {
            cancle!!.visibility = View.GONE
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        } else {
            cancle!!.visibility = View.VISIBLE
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        content!!.text = version.description
    }

    override fun initLayout(layoutParams: WindowManager.LayoutParams?) {
        super.initLayout(layoutParams)
        layoutParams!!.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    }

    private fun initListener() {
        confirm!!.setOnClickListener {
            if (isDownLoadSuccess) {
                install()
            } else {
                confirm!!.isClickable = false
                confirm!!.text = "正在下载..."
                downloadAPK()
            }
        }
        cancle!!.setOnClickListener { dismiss() }
    }

    private fun downloadAPK() {
        if (TextUtils.isEmpty(version.url)) {
            activity.showToastInfo("请去应用商店下载")
            confirm!!.text = "立即更新"
            confirm!!.isClickable = true
            dismiss()
            return
        }
        PermissionUtils.permission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
            .callback(object : PermissionUtils.SimpleCallback {
                @RequiresApi(Build.VERSION_CODES.FROYO)
                override fun onGranted() {
                    downApk()
                }

                override fun onDenied() {
                    error(activity, "无存储权限，无法更新")!!.show()
                    dismiss()
                    if (forceUpdate == FORCE_UPDATE_TRUE) {
                        if (isExit) AppUtils.exitApp()
                    }
                }
            })
            .request()
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun downApk() {

        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            // 获得存储卡的路径
//            sdPath = Environment.getExternalStorageDirectory() + File.separator + "runElephant";
            sdPath = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + File.separator + "runElephant"
            val file = File(sdPath)
            // 判断文件目录是否存在
            if (!file.exists()) {
                file.mkdir()
            }
            val apkFile = File(sdPath, "runElephant.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }
        }
        lineProgress!!.visibility = View.VISIBLE
        val destPath = "$sdPath/elephantoil.apk"

        rxLifeScope.launch {
            RxHttp.get(version.url)
                .toDownload(destPath, Dispatchers.Main) {
                    //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                    val currentProgress: Int = it.progress //当前进度 0-100
                    var currentSize = it.currentSize //当前已下载的字节大小
                    var totalSize = it.totalSize     //要下载的总字节大小
                    lineProgress!!.progress = currentProgress
                }
                .awaitResult {
                    //s为String类型，这里为文件存储路径
                    //下载完成，处理相关逻辑
                    install()
                    isDownLoadSuccess = true
                    confirm!!.isClickable = true
                    confirm!!.text = "立即安装"
                }
                .onFailure {
                    //下载失败，处理相关逻辑
                    confirm!!.isClickable = true
                    confirm!!.text = "立即更新"
                    activity.showToastWarning("下载失败,请稍后重试")
                }
        }
    }

    private fun install() {
        val file = File(sdPath, "elephantoil.apk")
        AppUtils.installApp(file)
    }

    companion object {
        private const val FORCE_UPDATE_TRUE = 1
    }

}