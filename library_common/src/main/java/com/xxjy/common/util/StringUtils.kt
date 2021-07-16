package com.xxjy.common.util

import android.R
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import kotlin.experimental.xor

object StringUtils {
    /**
     * 将price 进行位数删除后 转换成合适的price  多于两位舍去后面部分 ,同时把小数点末位的0去掉
     *
     * @param price
     * @return
     */
    fun getGreatPrice(price: Double): String {
        val strPrice = price.toString()
        return if (strPrice.contains("E") || strPrice.contains("e")) {
            getGreatPrice(
                BigDecimal(strPrice).toPlainString()
            )
        } else getGreatPrice(price.toString())
    }

    /**
     * 默认保留2位
     *
     * @param price
     * @return
     */
    fun getSplitPrice(price: String): Array<String?> {
        return getSplitPriceEnd(price, ".00")
    }

    /**
     * 默认保留1位
     *
     * @param price
     * @return
     */
    fun getSplitPriceEndOne(price: String): Array<String?> {
        return getSplitPriceEnd(price, ".0")
    }

    /**
     * 默认保留0位
     *
     * @param price
     * @return
     */
    fun getSplitPriceEndZero(price: String): Array<String?> {
        return getSplitPriceEnd(price, "")
    }

    /**
     * @param price
     * @return
     */
    fun getSplitPriceEnd(price: String, endValue: String?): Array<String?> {
        val result = arrayOfNulls<String>(2)
        if (!TextUtils.isEmpty(price)) {
            val priceSplit = price.split("\\.".toRegex()).toTypedArray()
            result[0] = priceSplit[0]
            if (priceSplit.size == 2) {
                result[1] = "." + priceSplit[1]
            } else {
                result[1] = endValue
            }
        } else {
            result[0] = "0"
            result[1] = endValue
        }
        return result
    }

    fun getGreatPrice(price: String): String {
        return try {
            val split = price.split("\\.".toRegex()).toTypedArray()
            if (split.size == 1) {    //说明是整数
                return price
            }
            var newPrice: String
            newPrice = if (split[1].length > 2) {    //说明很长
                split[1].substring(0, 2)
            } else {
                split[1]
            }
            if (newPrice.toInt() == 0) {
                split[0]
            } else {
                newPrice = removeEndZero(newPrice)
                split[0] + "." + newPrice
            }
        } catch (e: Exception) {
            LogUtils.e(e.message)
            price
        }
    }

    //将字符串尾数为0 的字符去掉
    private fun removeEndZero(newPrice: String): String {
        var newPrice = newPrice
        return if (newPrice.endsWith("0")) {
            newPrice = newPrice.substring(0, newPrice.length - 1)
            removeEndZero(newPrice)
        } else {
            newPrice
        }
    }

    /**
     * 将价格进行四舍五入最多保留两位进行处理, 截取规则为 多余两位四舍五入保留两位, 少于两位保留当前,同时把小数点末位的0去掉
     *
     * @param price
     * @return
     */
    fun getGreatHalfUp(price: Double): String {
        val discountHalfup = getDiscountHalfup(price, 2)
        val result = BigDecimal(discountHalfup).toDouble()
        return getGreatPrice(result)
    }

    fun getGreatHalfUp(price: String?): String {
        return getGreatHalfUp(BigDecimal(price).toDouble())
    }

    /**
     * 将价格进行四舍五入最多保留两位 , 最少保留一位 进行处理
     *
     * @param price
     * @return
     */
    fun getGreatHalfUpEndOne(price: Double): String {
//        String greatHalfUp = getGreatHalfUp(price);
//        if (!greatHalfUp.contains(".")) {
//            return greatHalfUp + ".0";
//        }
//        return greatHalfUp;

        //全部保留两位小数
        return getDiscountHalfup(price, 2)
    }

    fun getGreatHalfUpEndOne(price: String?): String {
        return if (TextUtils.isEmpty(price)) "" else getGreatHalfUpEndOne(
            BigDecimal(price).toDouble()
        )
    }

    //向上取整
    fun getDiscountUp(d: Double, no: Int): String {
        //no代表保留no位小数
        val b = BigDecimal(d)
        val f1 = b.setScale(no, BigDecimal.ROUND_HALF_UP).toDouble()
        if (no == 2) {
            return DecimalFormat("0.00").format(f1)
        }
        return if (no == 1) {
            DecimalFormat("0.0").format(f1)
        } else f1.toString() + ""
    }

    //四舍五入
    fun getDiscountHalfup(d: Double, no: Int): String {
        //no代表保留no位小数
        val b = BigDecimal(d)
        val f1 = b.setScale(no, BigDecimal.ROUND_HALF_UP)
        if (no == 2) {
            return DecimalFormat("0.00").format(f1)
        }
        if (no == 1) {
            return DecimalFormat("0.0").format(f1)
        }
        return if (no == 0) {
            DecimalFormat("0").format(f1)
        } else f1.toString() + ""
    }

    /**
     * 一键加油中处理输入的数字
     *
     * @param price
     * @return
     */
    fun checkPrice(price: String): String {
        if (TextUtils.isEmpty(price)) {
            return ""
        }
        var result: String
        result = if (price.startsWith(".")) {
            "0$price"
        } else {
            getGoodPrice(price)
        }
        var d = 0.0
        try {
            d = result.toDouble()
            if (d > 100000) {
                result = "100000"
            }
        } catch (e: Exception) {
            return "0"
        }
        if (!result.contains(".") && d == 0.0) {
            result = "0"
        }
        return result
    }

    /**
     * 创建一个图片选择器
     *
     * @param selectedState        普通状态的图片
     * @param unSelectedStateState 选中的状态
     */
    fun createSelector(
        selectedState: Drawable?,
        unSelectedStateState: Drawable?
    ): StateListDrawable {
        val bg = StateListDrawable()
        bg.addState(intArrayOf(R.attr.state_selected), selectedState)
        bg.addState(intArrayOf(), unSelectedStateState)
        return bg
    }

    /**
     * 对字符串中的点进行处理, 最多保留两位小数
     *
     * @param price
     * @return
     */
    private fun getGoodPrice(price: String): String {
        if (TextUtils.isEmpty(price)) {
            return ""
        }
        val split = price.split("\\.".toRegex()).toTypedArray()
        if (split.size == 2) {
            var newPrice = ""
            newPrice = if (split[1].length > 2) {    //说明很长
                split[1].substring(0, 2)
            } else {
                split[1]
            }
            return if (split[0].toInt() == 0) {
                "0.$newPrice"
            } else {
                split[0] + "." + newPrice
            }
        }
        return price
    }

    /**
     * 通过url地址字符串获取值为key的参数内容
     *
     * @param key
     * @return
     */
    fun getUrlDate(url: String, key: String): String? {
        var url = url
        if (TextUtils.isEmpty(url)) {
            return null
        }
        val firstIndex = url.indexOf("?")
        return if (url.length > firstIndex + 1) {
            url = url.substring(firstIndex + 1)
            getUrlKeyDate(url, key)
        } else {
            null
        }
    }

    /**
     * 将base64加密过的内容进行解密
     *
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.FROYO)
    fun decodePwd(pwd: String?): String? {
        if (TextUtils.isEmpty(pwd)) {
            return pwd
        }
        val bytes = Base64.decode(pwd, Base64.DEFAULT)
        for (i in bytes.indices) {
            bytes[i] = (bytes[i] xor 0x01) as Byte
        }
        var result: String? = null
        try {
            result = String(bytes, Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
        }
        return result
    }

    //通过url 和key 获取相应的数据
    private fun getUrlKeyDate(url: String, key: String): String? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        var result: String? = null
        val arrSplit = url.split("[&]".toRegex()).toTypedArray()
        for (strSplit in arrSplit) {
            val arrSplitEqual = strSplit.split("[=]".toRegex()).toTypedArray()
            //解析出键值
            if (arrSplitEqual.size > 1 && arrSplitEqual[0] == key) {
                //正确解析
                result = arrSplitEqual[1]
            }
        }
        return result
    }

    /**
     * 对卡片进行格式化,分成四个四个一组的卡号
     *
     * @param cardNo
     * @return
     */
    fun getFormatCardNo(cardNo: String): String {
        return getFormatCardNo(cardNo, 4)
    }

    /**
     * 对卡片进行格式化,分成everyFomatLength一组的卡号
     *
     * @param cardNo
     * @param everyFomatLength 每组的个数
     * @return
     */
    fun getFormatCardNo(cardNo: String, everyFomatLength: Int): String {
        val sb = StringBuilder()
        if (TextUtils.isEmpty(cardNo)) return sb.toString()
        val formatSize = cardNo.length / everyFomatLength
        if (formatSize == 0) return cardNo
        for (i in 0 until formatSize) {
            val startSplit = i * everyFomatLength
            sb.append(cardNo.substring(startSplit, startSplit + everyFomatLength))
            if (i != formatSize - 1) {
                sb.append(" ")
            }
        }
        val tailValue = cardNo.length % everyFomatLength
        if (tailValue != 0) {
            sb.append(" " + cardNo.substring(cardNo.length - tailValue, cardNo.length))
        }
        return sb.toString()
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    fun isContainsEmoji(source: String): Boolean {
        val len = source.length
        for (i in 0 until len) {
            val codePoint = source[i]
            if (!isEmojiCharacter(codePoint)) {     // 如果不能匹配,则该字符是Emoji表情
                return true
            }
        }
        return false
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return (codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA
                || codePoint.toInt() == 0xD
                || codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0xD7FF
                || codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFFFD
                || codePoint.toInt() >= 0x10000 && codePoint.toInt() <= 0x10FFFF)
    }

    /**
     * 对recycler列表的数据进行转换,数据给的是每 everyPageSize 个数据是横向的,因为recycler列表的排列顺序是竖向的,
     * 所以需要将数据进行转换来使竖向的排列达到横向的效果
     *
     * @param products
     * @param everyPageSize
     * @param <T>
     * @return
    </T> */
    fun <T> convertRecyclerDatas(products: List<T>, everyPageSize: Int): List<T> {
        val temp: MutableList<List<T>> = ArrayList()
        var pageSize = 1
        if (products.size > everyPageSize) {
            pageSize = products.size / everyPageSize
            val pageDetail = products.size % everyPageSize
            if (pageDetail != 0) {
                pageSize++
            }
        }
        if (pageSize == 1) {
            temp.add(products)
        } else {
            var currentData: MutableList<T>
            for (i in 0 until pageSize) {
                currentData = ArrayList()
                for (y in 0 until everyPageSize) {
                    val realPos = i * everyPageSize + y
                    if (realPos < products.size) {
                        currentData.add(products[realPos])
                    }
                }
                temp.add(currentData)
            }
        }
        val result: MutableList<T> = ArrayList()
        for (data in temp) {
            result.addAll(reverseToHorizontal(data))
        }
        return result
    }

    private fun <T> reverseToHorizontal(data: List<T>): List<T> {
        val result: MutableList<T> = ArrayList()
        var realPos: Int
        for (i in data.indices) {
            realPos = if (i % 2 == 0) {
                i / 2
            } else {
                (data.size + i) / 2
            }
            if (realPos < data.size) {
                result.add(data[realPos])
            }
        }
        return result
    }

    fun <T> disPathGroupDatas(products: List<T>?, everyPageSize: Int): List<List<T>> {
        val result: MutableList<List<T>> = ArrayList()
        if (products == null || products.isEmpty()) {
            return result
        }
        if (products.size <= everyPageSize) {
            result.add(products)
            return result
        }
        var pageSize = products.size / everyPageSize
        val pageReduce = products.size % everyPageSize
        if (pageReduce != 0) {
            pageSize += 1
        }
        var currentGroup: MutableList<T>
        for (i in 0 until pageSize) {
            currentGroup = ArrayList()
            for (y in 0 until everyPageSize) {
                val pos = i * everyPageSize + y
                if (pos < products.size) {
                    currentGroup.add(products[pos])
                }
            }
            result.add(currentGroup)
        }
        return result
    }

    fun checkPoint(num: String): String {
        var myNum = StringBuilder()
        try {
            val integerNum = num.substring(0, num.indexOf("."))
            val decimals = num.substring(num.indexOf("."), num.length)
            val decimalsNum = java.lang.Float.valueOf(decimals)
            myNum = if (decimalsNum > 0) {
                myNum.append(integerNum).append(decimals.substring(0, 2))
            } else {
                myNum.append(integerNum)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return myNum.toString()
    }

    fun checkPointDouble(num: String): String {
        var myNum = StringBuilder()
        try {
            val integerNum = num.substring(0, num.indexOf("."))
            val decimals = num.substring(num.indexOf("."), num.length)
            val decimalsNum = java.lang.Float.valueOf(decimals)
            myNum = if (decimalsNum > 0) {
                myNum.append(integerNum).append(decimals.substring(0, 3))
            } else {
                myNum.append(integerNum)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return myNum.toString()
    }
}