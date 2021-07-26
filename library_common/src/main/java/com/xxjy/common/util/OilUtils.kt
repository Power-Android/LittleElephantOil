package com.xxjy.common.util

import android.text.TextUtils
import com.xxjy.common.entity.OilEntity.StationsBean.OilPriceListBean

/**
 * @author power
 * @date 2/7/21 7:03 PM
 * @project ElephantOil
 * @description:
 */
object OilUtils {
    /**
     * 判断油号是否是汽油
     *
     * @param oilNumInfo
     * @return
     */
    fun isOilNumGas(oilNumInfo: OilPriceListBean): Boolean {
        val oilType = oilNumInfo.oilType
        return if (oilType == 0) {
            isOilNumGas(oilNumInfo.oilName)
        } else oilType == 1
        //汽油
    }

    //汽油
    private fun isOilNumGas(oilNum: String): Boolean {
        if (TextUtils.isEmpty(oilNum)) return false
        val oilNumList = arrayOf("90#", "92#", "93#", "95#", "97#", "98#", "101#", "68#")
        var result = false
        for (i in oilNumList.indices) {
            result = oilNumList[i] == oilNum
            if (result) {
                break
            }
        }
        return result
    }

    /**
     * 判断油号是否是柴油
     *
     * @param oilNumInfo
     * @return
     */
    fun isOilNumDiesel(oilNumInfo: OilPriceListBean): Boolean {
        val oilType = oilNumInfo.oilType
        return if (oilType == 0) {
            isOilNumDiesel(oilNumInfo.oilName)
        } else oilType == 2
        //柴油
    }

    //柴油
    private fun isOilNumDiesel(oilNum: String): Boolean {
        if (TextUtils.isEmpty(oilNum)) return false
        val oilNumList = arrayOf("-40#", "-35#", "-30#", "-20#", "-10#", "国四0#", "0#")
        var result = false
        for (i in oilNumList.indices) {
            result = oilNumList[i] == oilNum
            if (result) {
                break
            }
        }
        return result
    }

    /**
     * 判断油号是否是天然气
     *
     * @param oilNumInfo
     * @return
     */
    fun isOilNumNatural(oilNumInfo: OilPriceListBean): Boolean {
        val oilType = oilNumInfo.oilType
        return if (oilType == 0) {
            isOilNumNatural(oilNumInfo.oilName)
        } else oilType == 3
        //天然气
    }

    //天然气
    private fun isOilNumNatural(oilNum: String): Boolean {
        if (TextUtils.isEmpty(oilNum)) return false
        val oilNumList = arrayOf("CNG", "LNG")
        var result = false
        for (i in oilNumList.indices) {
            result = oilNumList[i] == oilNum
            if (result) {
                break
            }
        }
        return result
    }
}