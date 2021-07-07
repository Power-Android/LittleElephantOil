//package com.xxjy.shanyan;
//
//import android.content.Context;
//
//import com.blankj.utilcode.util.ActivityUtils;
//import com.blankj.utilcode.util.LogUtils;
//import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
//import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener;
//import com.chuanglan.shanyan_sdk.listener.InitListener;
//import com.xxjy.jyyh.constants.Constants;
//import com.xxjy.jyyh.ui.login.LoginActivity;
//
///**
// * ---------------------------
// * 闪验的管理器
// * <p>
// * https://flash.253.com/document/details?cid=93&lid=519&pc=28&pn=%25E9%2597%25AA%25E9%25AA%258CSDK#43f0e755
// */
//public class ShanYanManager {
//    private static final String APP_ID = "KlRtGJtQ";//okWJjwEH
//    private static final int INIT_SUCCESS_CODE = 1022;      //初始化成功的code
//    public static final int DEFAULT_SUCCESS_CODE = 1022;    //默认的流程成功code
//    private static int initCode = 0;
//    private static boolean isShanYanSupport = false;    //闪验是否支持
//
//    /**
//     * 初始化sdk ,第一步调用
//     *
//     * @param context
//     */
//    public static void initShanYnaSdk(Context context) {
//        if (!isInitSuccess()) {
//            //闪验SDK配置debug开关 （必须放在初始化之前，开启后可打印闪验SDK更加详细日志信息）
//            OneKeyLoginManager.getInstance().setDebug(Constants.IS_DEBUG);
//
//            //闪验SDK初始化（建议放在Application的onCreate方法中执行）
//            initShanyanSDK(context);
//        }
//    }
//
//    /**
//     * 判断是否已经初始化成功
//     *
//     * @return
//     */
//    public static boolean isInitSuccess() {
//        return initCode == INIT_SUCCESS_CODE;
//    }
//
//    /**
//     * 检查闪验是否支持
//     */
//    public static void checkShanYanSupportState() {
//        if (!isInitSuccess()) {
//            return;
//        }
//        if (isShanYanSupport()) {
//            return;
//        }
//        getPhoneInfo(new GetPhoneInfoListener() {
//            @Override
//            public void getPhoneInfoStatus(int code, String result) {
//                LogUtils.d("初始化： code==" + code + "   result==" + result);
//                isShanYanSupport = code == ShanYanManager.DEFAULT_SUCCESS_CODE;
//            }
//        });
//    }
//
//    public static boolean isShanYanSupport() {
//        return isShanYanSupport;
//    }
//
//    /**
//     * 预取号 ,第二步调用
//     *
//     * @param listener 结果监听
//     */
//    public static void getPhoneInfo(GetPhoneInfoListener listener) {
//        OneKeyLoginManager.getInstance().getPhoneInfo(listener);
//    }
//
//    /**
//     * 关闭闪验界面
//     */
//    public static void finishAuthActivity() {
//        OneKeyLoginManager.getInstance().finishAuthActivity();
//        OneKeyLoginManager.getInstance().removeAllListener();
//        ActivityUtils.finishActivity(LoginActivity.class);
//    }
//
//    /**
//     * 初始化
//     *
//     * @param context
//     */
//    private static void initShanyanSDK(Context context) {
//        OneKeyLoginManager.getInstance().init(context, APP_ID, new InitListener() {
//            @Override
//            public void getInitStatus(int code, String result) {
//                //闪验SDK初始化结果回调
//                LogUtils.w("初始化： code==" + code + "   result==" + result);
//                initCode = code;
//            }
//        });
//    }
//
//}
