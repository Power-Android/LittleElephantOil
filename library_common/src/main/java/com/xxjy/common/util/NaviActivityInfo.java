//package com.xxjy.common.util;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//
//import com.blankj.utilcode.util.EncodeUtils;
//import com.blankj.utilcode.util.ImageUtils;
//import com.xxjy.jyyh.base.BaseActivity;
//import com.xxjy.jyyh.dialog.CustomerServiceDialog;
//import com.xxjy.jyyh.ui.mine.MyCouponActivity;
//import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
//import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
//import com.xxjy.jyyh.ui.web.WebViewActivity;
//
//import java.io.File;
//
///**
// * 车主邦
// * ---------------------------
// * <p>
// * Created by zhaozh on 2019/1/25.
// */
//public class NaviActivityInfo {
//    private static final String SAVE_PICTURE_ERROR_MSG = "图片保存失败，请提供存储卡读写权限";
//
//    //跳转加油支付结果界面
//    public static final String NATIVE_TO_PAY_RESULT = "native=payResultAddOil";
//    //消息中心
//    public static final String NATIVE_TO_MSG_CENTER = "native=toMessageCenter";
//    //显示客服帮助的弹窗
//    public static final String NATIVE_SHOW_CALL_HELP = "native=customerService";
//
//    //跳转到客服聊天页面
//    public static final String NATIVE_TO_HELP_CHAT = "native=onlineService";
//
//    //跳转到客服打电话页面
//    public static final String NATIVE_TO_HELP_DIAL_PHONE = "native=dialPhoneService";
//
//    //优惠券列表
//    public static final String NATIVE_TO_MY_COUPON = "native=couponList";
//
//    //保存图片
//    public static final String NATIVE_SAVE_PICTURE = "native=savePicture";
//
//    /**
//     * 从Url提取intent信息并分发
//     *
//     * @param activity
//     * @param urlInfo
//     * @return
//     */
//    public static void disPathIntentFromUrl(BaseActivity activity, String urlInfo) {
//        if (TextUtils.isEmpty(urlInfo)) return;
//        if (urlInfo.contains(NATIVE_TO_PAY_RESULT)) {    //支付结果
//            String orderNo = getParams(urlInfo, "orderNo");
//            String orderId = getParams(urlInfo, "orderId");
//            RefuelingPayResultActivity.openPayResultPage(activity, orderNo, orderId);
//        } else if (urlInfo.contains(NATIVE_TO_HELP_CHAT) || urlInfo.contains(NATIVE_SHOW_CALL_HELP) || urlInfo.contains(NATIVE_TO_HELP_DIAL_PHONE)) {
//
//            CustomerServiceDialog customerServiceDialog = new CustomerServiceDialog(activity);
//            try {
//                customerServiceDialog.show(activity.getWindow().getDecorView().findViewById(android.R.id.content));
//            } catch (Exception e) {
//
//            }
//
//        } else if (urlInfo.contains(NATIVE_TO_MSG_CENTER)) {
//
//            activity.startActivity(new Intent(activity, MessageCenterActivity.class));
//
//        } else if (urlInfo.contains(NATIVE_TO_MY_COUPON)) {
//
//            activity.startActivity(new Intent(activity, MyCouponActivity.class));
//
//        } else if (urlInfo.contains(NATIVE_SAVE_PICTURE)) {
//            String imageString = getParams(urlInfo, "imageString");
//            if (!TextUtils.isEmpty(imageString)) {
//                try {
//                    int indexOf = imageString.indexOf(",");
//                    if (indexOf >= imageString.length() - 1) {
//                        activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                        return;
//                    }
//                    String result = imageString.substring(indexOf + 1);
////                    byte[] bytes = Base64.decode(result, Base64.DEFAULT);
//                    String startString = imageString.substring(0, indexOf);
//                    byte[] bytes = EncodeUtils.base64Decode(result);
//                    if (bytes != null) {
//                        Bitmap.CompressFormat formats = Bitmap.CompressFormat.JPEG;
//                        if (!TextUtils.isEmpty(startString)) {
//                            if (startString.contains("png")) {
//                                formats = Bitmap.CompressFormat.PNG;
//                            }
//                        }
//                        File file = ImageUtils.save2Album(ImageUtils.bytes2Bitmap(bytes), formats);
//                        if (file != null) {
//                            activity.showToastSuccess("保存成功，图片已保存至相册");
//                        } else {
//                            activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                        }
//                    }
//                } catch (Exception e) {
//                    activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                }
//            }
//        }
//        //默认的
//        else {
//            WebViewActivity.openRealUrlWebActivity(activity, urlInfo);
//        }
//    }
//
//    //从link中获取key值, 取值位置是 链接?之后的 key=value 的value值
//    public static String getParams(String link, String key) {
//        String result = "";
//        if (TextUtils.isEmpty(link)) return result;
//
//        if (link.contains("?")) {
//            if (link.length() > link.indexOf("?") + 1) {
//                link = link.substring(link.indexOf("?") + 1);
//            }
//        }
//        String regex = key + "=";
//        String[] split = link.split("&");
//        for (int i = 0; i < split.length; i++) {
//            if (split[i].startsWith(regex)) {
//                result = split[i].substring(regex.length());
//                break;
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 获取link ? 和 & 符号之前中目标 tagStr 字符串之后 第 nextPosition 个值, 取值位置是 链接?之前的
//     * link/result1/result2 中的 result1值或者result2值, 通过nextPosition来确定取哪个值
//     *
//     * @param link
//     * @param tagStr
//     * @param nextPosition
//     * @return
//     */
//    private static String parseUrlParams(String link, String tagStr, int nextPosition) {
//        String result = "";
//        if (TextUtils.isEmpty(link)) return result;
//
//        if (link.contains("?")) {
//            if (link.length() > link.indexOf("?")) {
//                link = link.substring(0, link.indexOf("?"));
//            }
//        }
//        if (link.contains("&")) {
//            if (link.length() > link.indexOf("&")) {
//                link = link.substring(0, link.indexOf("&"));
//            }
//        }
//        String[] split = link.split("\\/");
//        for (int i = 0; i < split.length; i++) {
//            if (split[i].equals(tagStr)) {
//                if (split.length > i + nextPosition) {
//                    result = split[i + nextPosition];
//                }
//                break;
//            }
//        }
//        return result;
//    }
//}
