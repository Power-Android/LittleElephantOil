//package com.xxjy.common.util;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.provider.Settings;
//
//import androidx.annotation.RequiresApi;
//
//import com.blankj.utilcode.util.AppUtils;
//import com.xxjy.common.base.BaseActivity;
//
//import java.io.File;
//
///**
// * @Description:APP管理类
// * @date 2015-4-11
// */
//public class AppManager {
//
//    private AppManager() {
//    }
//
//    /**
//     * 打开安装包
//     *
//     * @param activity
//     * @param apkFile
//     */
//    public static void installApp(BaseActivity activity, File apkFile) {
//        // 核心是下面几句代码
//        if (apkFile == null) return;
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
//                if (hasInstallPermission) {
//                    AppUtils.installApp(apkFile);
//                } else {
//                    //请求安装未知应用来源的权限
////                    ActivityCompat.requestPermissions(activity,
////                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 0);
//                    activity.showToastWarning("请赋予权限进行安装新版本");
//                    startInstallPermissionSettingActivity(activity);
//                }
//            } else {
//                AppUtils.installApp(apkFile);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//            activity.showToastWarning(e.getMessage());
////            activity.showToastWarning("安装失败,请自行跳转应用市场进行更新");
//        }
//    }
//
//    /**
//     * 跳转到设置-允许安装未知来源-页面
//     *
//     * @param activity
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private static void startInstallPermissionSettingActivity(BaseActivity activity) {
//        //注意这个是8.0新API
//        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//    }
//
//
//    /**
//     * 获取 app 中的 渠道 channel 值
//     *
//     * @return
//     */
//    public static String getAppMetaChannel() {
//        return getAppMetaData(App.getContext(), "UMENG_CHANNEL");
//    }
//
//    /**
//     * 获取application中指定的meta-data
//     *
//     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
//     */
//    public static String getAppMetaData(Context ctx, String metaName) {
//        String resultData = null;
//        try {
//            PackageManager packageManager = ctx.getPackageManager();
//            if (packageManager != null) {
//                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
//                if (applicationInfo != null) {
//                    if (applicationInfo.metaData != null) {
//                        resultData = applicationInfo.metaData.getString(metaName);
//                    }
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return resultData;
//    }
//
//}