//package com.xxjy.common.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//
//import com.blankj.utilcode.util.SPUtils;
//import com.xxjy.jyyh.constants.SPConstants;
//import com.xxjy.jyyh.ui.login.LoginActivity;
//import com.xxjy.jyyh.utils.umengmanager.UMengManager;
//
///**
// * @author power
// * @date 12/4/20 3:31 PM
// * @project RunElephant
// * @description:
// */
//public class LoginHelper {
//    public static CallBack callBack;
//
//    public static void login(Context context, CallBack loginCallBack) {
//        if (!SPUtils.getInstance().getBoolean(SPConstants.LOGIN_STATUS)) {
//            callBack = loginCallBack;
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//        } else {
//            if (loginCallBack != null) {
//                loginCallBack.onLogin();
//            }
//        }
//    }
//
//    public static void loginOut(Activity activity) {
//        SPUtils.getInstance().put(SPConstants.LOGIN_STATUS,false);
//        SPUtils.getInstance().put(SPConstants.MOBILE,"");
//        SPUtils.getInstance().put(SPConstants.APP_TOKEN,"");
//        SPUtils.getInstance().put(SPConstants.USER_TYPE,-1);
//        SPUtils.getInstance().put(SPConstants.OPEN_ID,"");
//        SPUtils.getInstance().put(SPConstants.NEW_USER_RED_PACKET,0l);
//        UMengManager.onProfileSignOff();
//        if (activity != null){
//            activity.finish();
//        }
//    }
//
//    public interface CallBack {
//        void onLogin();
//    }
//}
