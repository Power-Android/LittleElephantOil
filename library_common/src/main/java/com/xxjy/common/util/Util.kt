package com.xxjy.common.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.xxjy.common.base.BaseActivity
import java.io.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object Util {
    private const val TAG = "SDK_Sample.Util"
    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun getHtmlByteArray(url: String?): ByteArray? {
        var htmlUrl: URL? = null
        var inStream: InputStream? = null
        try {
            htmlUrl = URL(url)
            val connection = htmlUrl.openConnection()
            val httpConnection =
                connection as HttpURLConnection
            val responseCode = httpConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.inputStream
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return inputStreamToByte(inStream)
    }

    fun inputStreamToByte(`is`: InputStream?): ByteArray? {
        try {
            val bytestream = ByteArrayOutputStream()
            var ch: Int
            while (`is`!!.read().also { ch = it } != -1) {
                bytestream.write(ch)
            }
            val imgdata = bytestream.toByteArray()
            bytestream.close()
            return imgdata
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun readFromFile(fileName: String?, offset: Int, len: Int): ByteArray? {
        var len = len
        if (fileName == null) {
            return null
        }
        val file = File(fileName)
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found")
            return null
        }
        if (len == -1) {
            len = file.length().toInt()
        }
        Log.d(
            TAG,
            "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len)
        )
        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:$offset")
            return null
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:$len")
            return null
        }
        if (offset + len > file.length().toInt()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length())
            return null
        }
        var b: ByteArray? = null
        try {
            val `in` = RandomAccessFile(fileName, "r")
            b = ByteArray(len) // 创建合适文件大小的数组
            `in`.seek(offset.toLong())
            `in`.readFully(b)
            `in`.close()
        } catch (e: Exception) {
            Log.e(TAG, "readFromFile : errMsg = " + e.message)
            e.printStackTrace()
        }
        return b
    }

    fun parseInt(string: String?, def: Int): Int {
        try {
            return if (string == null || string.length <= 0) def else string.toInt()
        } catch (e: Exception) {
        }
        return def
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    fun compareVersion(version1: String, version2: String): Int {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
            return 0
        }
        if (version1 == version2) {
            return 0
        }
        val version1Array = version1.split("\\.".toRegex()).toTypedArray()
        val version2Array = version2.split("\\.".toRegex()).toTypedArray()
        // 获取最小长度值
        val minLen = Math.min(version1Array.size, version2Array.size)
        var diff = 0
        try {
            for (i in 0 until minLen) {
                diff = version1Array[i].toInt() - version2Array[i].toInt()
                if (diff != 0) {
                    break
                }
            }
        } catch (e: Exception) {
            return 0
        }
        if (diff == 0) {
            diff = version1Array.size - version2Array.size
        }
        return if (diff > 0) {
            1
        } else if (diff == 0) {
            0
        } else {
            -1
        }
    }

    //版本号
    val versionName: String
        get() {
            val appVersionName = AppUtils.getAppVersionName()
            return if (TextUtils.isDigitsOnly(appVersionName)) "" else appVersionName
        }

    fun formatDouble(d: Double): String {
        val bg = BigDecimal.valueOf(d).setScale(2, RoundingMode.DOWN)
        val num = bg.toDouble()
        if (Math.round(num) - num == 0.0) {
            return num.toString()
        }
        return num.toString()
    }

    /**
     * 跳转拨打电话界面
     *
     * @param activity
     * @param phoneNumber
     * @return
     */
    fun toDialPhoneAct(activity: Context, phoneNumber: String): Boolean {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false
        }
        try {
            val phoneUri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_DIAL, phoneUri)
            if (intent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(intent)
                return true
            }
        } catch (e: Exception) {
        }
        (activity as BaseActivity).showToastWarning("您的设备无法拨打电话")
        return false
    }
}