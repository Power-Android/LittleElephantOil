package com.xxjy.common.http

import android.text.TextUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.EncodeUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author power
 * @date 12/9/20 11:52 AM
 * @project RunElephant
 * @description:
 */
object HeaderUtils {
    fun sortMapByKey(oriMap: MutableMap<String, String>?): Map<String, String>? {
        if (oriMap == null || oriMap.isEmpty()) {
            return null
        }
        oriMap["rsign"] = getRandomString(15)
        oriMap["did"] = DeviceUtils.getUniqueDeviceId()
        val sortMap: MutableMap<String, String> = TreeMap { o1: String, o2: String? ->
            o1.compareTo(
                o2!!
            )
        }
        for ((key, value) in oriMap) {
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) continue
            sortMap[key] = value
        }
        return sortMap
    }

    /**
     * @param length
     * @return 生成随机字符串
     */
    private fun getRandomString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number = random.nextInt(62)
            sb.append(str[number])
        }
        return sb.toString()
    }

    fun getSign(map: Map<String, String>?): String? {
        var sign = ""
        if (map != null) {
            val entries = map.entries
            for ((key, value) in entries) {
                sign += "&$key="
                sign += value
            }
        }
        val subSign = sign.substring(1)
        //        LogUtils.e("加密前的字符串：" + subSign);
        val ALGORITHM = "HmacSHA1"
        val ENCODING = "UTF-8"
        val apiKey = "Orvay1rVsoU9nlpY"
        var mac: Mac? = null
        try {
            mac = Mac.getInstance(ALGORITHM)
            mac.init(SecretKeySpec(apiKey.toByteArray(charset(ENCODING)), ALGORITHM))
            val signData = mac.doFinal(subSign.toByteArray(charset(ENCODING)))
            sign = String(EncodeUtils.base64Encode(signData))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //        LogUtils.e("加密后的字符串：" + sign);
        return percentEncode(sign)
    }

    private const val ENCODING = "UTF-8"
    private fun percentEncode(value: String?): String? {
        var replace = ""
        try {
            replace = URLEncoder.encode(value, ENCODING)
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return if (value != null) replace else null
    }
}