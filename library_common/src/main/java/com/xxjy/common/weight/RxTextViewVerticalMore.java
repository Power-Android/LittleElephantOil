package com.xxjy.common.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.xxjy.common.R;

import java.util.List;

/**
 * @author vondear
 * @date 2016/7/20
 * <p>
 * 仿淘宝首页的 淘宝头条滚动的自定义View
 */
public class RxTextViewVerticalMore extends ViewFlipper {

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 3000;
    /**
     * 动画时间
     */
    private int animDuration = 500;

    /**
     * 是否可以滑动
     */
    private boolean canFlipping = true;

    public RxTextViewVerticalMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 获取当前是否可以轮播
     *
     * @return
     */
    public boolean isCanFlipping() {
        return canFlipping;
    }

    /**
     * 设置是否可以轮播, 默认为true ,如果不可以轮播,则需要在 {@link #setViews(List)}之前调用
     *
     * @param canFlipping
     */
    public void setCanFlipping(boolean canFlipping) {
        this.canFlipping = canFlipping;
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(final List<View> views) {
        removeAllViews();
        if (views == null || views.size() == 0) return;
        for (int i = 0; i < views.size(); i++) {
            final int position = i;
            //设置监听回调
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, views.get(position));
                    }
                }
            });
            addView(views.get(i));
        }
        if (canFlipping) {
            startFlipping();
        } else {
            stopFlipping();
        }
    }

    /**
     * 点击
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
