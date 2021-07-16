package com.xxjy.common.util

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object GsonTool {
    fun <T> parseJson(jsonData: String?, type: Class<T>?): T {
        val gson = Gson()
        return gson.fromJson(jsonData, type)
    }

    fun toJson(o: Any?): String {
        val gson = Gson()
        return gson.toJson(o)
    }

    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager: AssetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

//    fun parseData(result: String?): ArrayList<JsonBean> { //Gson 解析
//        val detail: ArrayList<JsonBean> = ArrayList<JsonBean>()
//        try {
//            val data = JSONArray(result)
//            val gson = Gson()
//            for (i in 0 until data.length()) {
//                val entity: JsonBean =
//                    gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
//                detail.add(entity)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return detail
//    }
//
//    fun parseCityData(result: String?): ArrayList<CityJsonBean> {    //Gson 解析
//        val detail: ArrayList<CityJsonBean> = ArrayList<CityJsonBean>()
//        try {
//            val data = JSONArray(result)
//            val gson = Gson()
//            for (i in 0 until data.length()) {
//                val entity: CityJsonBean =
//                    gson.fromJson(data.optJSONObject(i).toString(), CityJsonBean::class.java)
//                detail.add(entity)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return detail
//    }
}