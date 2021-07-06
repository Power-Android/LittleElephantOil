//package com.xxjy.common.util;
//
//import android.content.Context;
//import android.content.res.AssetManager;
//
//import com.google.gson.Gson;
//import com.xxjy.jyyh.entity.picker.CityJsonBean;
//import com.xxjy.jyyh.entity.picker.JsonBean;
//
//import org.json.JSONArray;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class GsonTool {
//
//    public static <T> T parseJson(String jsonData, Class<T> type) {
//        Gson gson = new Gson();
//        T result = gson.fromJson(jsonData, type);
//        return result;
//    }
//
//    public static String toJson(Object o) {
//        Gson gson = new Gson();
//        String json = gson.toJson(o);
//        return json;
//    }
//
//    public static String getJson(Context context, String fileName) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            AssetManager assetManager = context.getAssets();
//            BufferedReader bf = new BufferedReader(new InputStreamReader(
//                    assetManager.open(fileName)));
//            String line;
//            while ((line = bf.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//    }
//
//    public static ArrayList<JsonBean> parseData(String result) {//Gson 解析
//        ArrayList<JsonBean> detail = new ArrayList<>();
//        try {
//            JSONArray data = new JSONArray(result);
//            Gson gson = new Gson();
//            for (int i = 0; i < data.length(); i++) {
//                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
//                detail.add(entity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return detail;
//    }
//
//    public static ArrayList<CityJsonBean> parseCityData(String result) {    //Gson 解析
//        ArrayList<CityJsonBean> detail = new ArrayList<>();
//        try {
//            JSONArray data = new JSONArray(result);
//            Gson gson = new Gson();
//            for (int i = 0; i < data.length(); i++) {
//                CityJsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityJsonBean.class);
//                detail.add(entity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return detail;
//    }
//}
