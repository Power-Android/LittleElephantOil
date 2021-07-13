package com.xxjy.common.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.appcompat.widget.AppCompatImageButton;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * @author power
 * @date 2021/6/9 2:57 下午
 * @project ElephantOil
 * @description:
 */
public class DragView extends AppCompatImageButton {
    private int screenWidth;
    private int screenHeight;
    private int topMargin = 0;      //距离顶部活动距离
    private int bottomMargin = 0;   //距离底部活动距离

    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();
    }

    private int lastX;
    private int lastY;

    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
//        LogUtils.e("rawX=" + rawX + "  --  rawY=" + rawY);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                clearAnimation();
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                if (!isDrag) {
                    //这里修复一些华为手机无法触发点击事件的问题
                    int distance = (int) Math.sqrt(dx * dx + dy * dy);
                    int TouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                    if (distance >= TouchSlop) {
                        isDrag = true;
                    }
                }
                if (isDrag) {
                    float x = getX() + dx;
                    float y = getY() + dy;
                    //检测是否到达边缘 左上右下
                    x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                    y = y < topMargin ? topMargin : y > screenHeight - bottomMargin - getHeight() ? screenHeight - bottomMargin - getHeight() : y;
                    setX(x);
                    setY(y);
                    lastX = rawX;
                    lastY = rawY;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    int centerCurrent = screenWidth / 2;
                    int postionX = 0;
                    int disTence = 0;
                    if (rawX > centerCurrent) { //在右边,向右
                        postionX = screenWidth - getWidth();
                        disTence = screenWidth - getWidth() - rawX;
                    } else {
                        postionX = 0;
                        disTence = rawX;
                    }
                    long time = 1L * 300 * disTence / centerCurrent;
                    if (disTence != 0 && time != 0) {
                        animate().x(postionX).setDuration(Math.abs(time)).start();
                    }
                }
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
//        return true;
    }

    /*************************/
    /**  getter and setter  **/
    /*************************/
    public int getTopMargin() {
        return topMargin;
    }

    public DragView setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public DragView setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }
}
