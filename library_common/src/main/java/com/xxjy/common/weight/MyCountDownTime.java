package com.xxjy.common.weight;

import android.os.CountDownTimer;

/**
 * @author power
 * @date 12/2/20 3:55 PM
 * @project RunElephant
 * @description:
 */
public class MyCountDownTime extends CountDownTimer {
    private OnTimeCountDownListener listener;
    private long millisUntilFinished = 0;

    public static MyCountDownTime getInstence(long millisInFuture, long countDownInterval) {
        return new MyCountDownTime(millisInFuture, countDownInterval);
    }

    private MyCountDownTime(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.millisUntilFinished = millisUntilFinished;
        if (listener != null) {
            listener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        this.millisUntilFinished = 0;
        if (listener != null) {
            listener.onFinish();
        }
    }

    /**
     * 判断当前的倒计时活动是否已经完毕
     *
     * @return true 代表已经完毕, false代表尚未完毕
     */
    public boolean isFinished() {
        return millisUntilFinished <= 0;
    }

    /**
     * 提供该外部方法来停止,内部会cancel掉活动并调用onFinish
     */
    public void stopCountDown() {
        cancel();
        onFinish();
    }

    public void setOnTimeCountDownListener(OnTimeCountDownListener listener) {
        this.listener = listener;
    }

    public interface OnTimeCountDownListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }
}
