package com.xxjy.common.weight;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * create dd
 * date 2019-11-19
 * email dd@163.com
 *
 * @author sunxiaokun
 */
public class SettingLayout extends AppCompatTextView implements IPagerTitleView {

    private int mSelectedColor;
    private int mNormalColor;
    private int mTextSize = 16;
    private int mSelectTextSize = 20;
    private boolean mIsBold=false;

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public SettingLayout(Context context) {
        this(context, null);
    }

    public SettingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(17);
    }

    public void setmSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    public void setmNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }
    public void setSelectedStyle(boolean isBold){
        mIsBold = isBold;
    }

    public void setTextViewSize(int normalSize, int selectSize) {
        this.mTextSize = normalSize;
        this.mSelectTextSize = selectSize;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        this.setTextColor(mSelectedColor);
        setTextSize(mSelectTextSize);
        if(mIsBold){
            this.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            this.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        this.setTextColor(mNormalColor);
        setTextSize(mTextSize);
            this.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

    }


    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }
}
