package com.xxjy.common.util.toastlib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/9/12.
 * <p>
 * 这是一个 管理 toast 弹窗的工具类
 */

public class MyToast {
    private static Toast mToast;

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 显示一个普通的toast
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    /**
     * 显示一个普通的长时间toast
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }

    /**
     * 屏幕中心展示一个toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showContentToast(Context context, String myToastShow) {
        showNormal(context, myToastShow);
    }

    /**
     * 展示 普通的 toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showNormal(Context context, String myToastShow) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_NORMAL);
    }

    /**
     * 展示 信息级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showInfo(Context context, String myToastShow) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_INFO);
    }

    /**
     * 展示 警告级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showWarning(Context context, String myToastShow) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_WARNING);
    }

    /**
     * 展示 成功级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showSuccess(Context context, String myToastShow) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_SUCCESS);
    }

    /**
     * 展示 错误级别的 toast
     *
     * @param context
     * @param myToastShow
     */
    public static void showError(Context context, String myToastShow) {
        showLeavelToast(context, myToastShow, ToastType.TYPE_ERROR);
    }

    /**
     * 通过不同的类型来展示不同的级别的toast
     *
     * @param context
     * @param myToastShow
     * @param toastyType
     */
    public static void showLeavelToast(Context context, String myToastShow, ToastType toastyType) {
        switch (toastyType) {
            case TYPE_NORMAL:
                runOnUiThread(() -> Toasty.normal(context, myToastShow).show());
                break;
            case TYPE_INFO:
                runOnUiThread(() -> Toasty.info(context, myToastShow).show());
                break;
            case TYPE_WARNING:
                runOnUiThread(() -> Toasty.warning(context, myToastShow).show());
                break;
            case TYPE_SUCCESS:
                runOnUiThread(() -> Toasty.success(context, myToastShow).show());
                break;
            case TYPE_ERROR:
                runOnUiThread(() -> Toasty.error(context, myToastShow).show());
                break;
        }
    }

    private static void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }
}
