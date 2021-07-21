package com.xxjy.common.weight.autocodeedittextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.xxjy.common.R
import com.xxjy.common.weight.autocodeedittextview.VerificationAction.OnVerificationCodeChangedListener

/**
 * 验证码的EditText
 * Created by yj on 2017/6/12.
 *
 *
 * 源码: https://github.com/JustKiddingBaby/VercodeEditText
 */
class VerCodeEditText @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(
    context!!, attrs, defStyleAttr
), VerificationAction, TextWatcher {
    private var mFigures //需要输入的位数
            = 0
    private var mVerCodeMargin //验证码之间的间距
            = 0
    private var mBottomSelectedColor //底部选中的颜色
            = 0
    private var mBottomNormalColor //未选中的颜色
            = 0
    private var mBottomLineHeight = 0f //底线的高度
    private var mBackgroundRadios = 0f //背景颜色的圆角
    private var mSelectedBackgroundColor //选中的背景颜色
            = 0
    private var mNormalBackgroundColor //未选中的背景颜色
            = 0
    private var onCodeChangedListener: OnVerificationCodeChangedListener? = null
    private var mCurrentPosition = 0
    private var mEachRectLength = 0 //每个矩形的边长
    private var mSelectedBackgroundPaint: Paint? = null
    private var mNormalBackgroundPaint: Paint? = null
    private var mBottomSelectedPaint: Paint? = null
    private var mBottomNormalPaint: Paint? = null

    /**
     * 初始化paint
     */
    private fun initPaint() {
        mSelectedBackgroundPaint = Paint()
        mSelectedBackgroundPaint!!.color = mSelectedBackgroundColor
        mNormalBackgroundPaint = Paint()
        mNormalBackgroundPaint!!.color = mNormalBackgroundColor
        mBottomSelectedPaint = Paint()
        mBottomNormalPaint = Paint()
        mBottomSelectedPaint!!.color = mBottomSelectedColor
        mBottomNormalPaint!!.color = mBottomNormalColor
        mBottomSelectedPaint!!.strokeWidth = mBottomLineHeight
        mBottomNormalPaint!!.strokeWidth = mBottomLineHeight
    }

    /**
     * 初始化Attrs
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VerCodeEditText)
        mFigures = ta.getInteger(R.styleable.VerCodeEditText_figures, 4)
        mVerCodeMargin = ta.getDimension(R.styleable.VerCodeEditText_verCodeMargin, 0f).toInt()
        mBottomSelectedColor = ta.getColor(
            R.styleable.VerCodeEditText_bottomLineSelectedColor,
            currentTextColor
        )
        mBottomNormalColor = ta.getColor(
            R.styleable.VerCodeEditText_bottomLineNormalColor,
            getColor(android.R.color.darker_gray)
        )
        mBottomLineHeight = ta.getDimension(R.styleable.VerCodeEditText_bottomLineHeight, 0f)
        mBackgroundRadios = ta.getDimension(R.styleable.VerCodeEditText_backgroundRadios, 0f)
        mSelectedBackgroundColor = ta.getColor(
            R.styleable.VerCodeEditText_selectedBackgroundColor,
            getColor(android.R.color.darker_gray)
        )
        mNormalBackgroundColor = ta.getColor(
            R.styleable.VerCodeEditText_normalBackgroundColor,
            getColor(android.R.color.darker_gray)
        )
        ta.recycle()

        // force LTR because of bug: https://github.com/JustKiddingBaby/VercodeEditText/issues/4
        layoutDirection = LAYOUT_DIRECTION_LTR
    }

    override fun setCursorVisible(visible: Boolean) {
        super.setCursorVisible(true) //隐藏光标的显示
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthResult = 0
        var heightResult = 0
        //最终的宽度
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        widthResult = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            getScreenWidth(context)
        }
        //每个矩形形的宽度
        mEachRectLength = (widthResult - mVerCodeMargin * (mFigures - 1)) / mFigures
        //最终的高度
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        heightResult = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            mEachRectLength
        }
        setMeasuredDimension(widthResult, heightResult)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            requestFocus()
            setSelection(text!!.length)
            showKeyBoard(context)
            return false
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        mCurrentPosition = text!!.length
        val width = mEachRectLength - paddingLeft - paddingRight
        val height = measuredHeight - paddingTop - paddingBottom
        for (i in 0 until mFigures) {
            canvas.save()
            val start = width * i + i * mVerCodeMargin
            val end = width + start
            //画一个矩形
            if (i == mCurrentPosition) { //选中的下一个状态
                if (mBackgroundRadios > 0 && Build.VERSION.SDK_INT > 21) {
                    canvas.drawRoundRect(
                        start.toFloat(),
                        0f,
                        end.toFloat(),
                        height.toFloat(),
                        mBackgroundRadios,
                        mBackgroundRadios,
                        mSelectedBackgroundPaint!!
                    )
                } else {
                    canvas.drawRect(
                        start.toFloat(),
                        0f,
                        end.toFloat(),
                        height.toFloat(),
                        mSelectedBackgroundPaint!!
                    )
                }
            } else {
                if (mBackgroundRadios > 0 && Build.VERSION.SDK_INT > 21) {
                    canvas.drawRoundRect(
                        start.toFloat(),
                        0f,
                        end.toFloat(),
                        height.toFloat(),
                        mBackgroundRadios,
                        mBackgroundRadios,
                        mNormalBackgroundPaint!!
                    )
                } else {
                    canvas.drawRect(
                        start.toFloat(),
                        0f,
                        end.toFloat(),
                        height.toFloat(),
                        mNormalBackgroundPaint!!
                    )
                }
            }
            canvas.restore()
        }
        //绘制文字
        val value = text.toString()
        for (i in 0 until value.length) {
            canvas.save()
            val start = width * i + i * mVerCodeMargin
            val x = (start + width / 2).toFloat()
            val paint = paint
            paint.textAlign = Paint.Align.CENTER
            paint.color = currentTextColor
            val fontMetrics = paint.fontMetrics
            val baseline = ((height - fontMetrics.bottom + fontMetrics.top) / 2
                    - fontMetrics.top)
            canvas.drawText(value[i].toString(), x, baseline, paint)
            canvas.restore()
        }
        //绘制底线
        if (mBottomLineHeight > 0) {
            for (i in 0 until mFigures) {
                canvas.save()
                val lineY = height - mBottomLineHeight / 2
                val start = width * i + i * mVerCodeMargin
                val end = width + start
                if (i <= mCurrentPosition) {
                    canvas.drawLine(
                        start.toFloat(),
                        lineY,
                        end.toFloat(),
                        lineY,
                        mBottomSelectedPaint!!
                    )
                } else {
                    canvas.drawLine(
                        start.toFloat(),
                        lineY,
                        end.toFloat(),
                        lineY,
                        mBottomNormalPaint!!
                    )
                }
                canvas.restore()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        mCurrentPosition = text!!.length
        postInvalidate()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mCurrentPosition = text!!.length
        postInvalidate()
        if (onCodeChangedListener != null) {
            onCodeChangedListener!!.onVerCodeChanged(text, start, before, count)
        }
    }

    override fun afterTextChanged(s: Editable) {
        mCurrentPosition = text!!.length
        postInvalidate()
        if (text!!.length == mFigures) {
            if (onCodeChangedListener != null) {
                onCodeChangedListener!!.onInputCompleted(text)
            }
        } else if (text!!.length > mFigures) {
            text!!.delete(mFigures, text!!.length)
        }
    }

    override fun setFigures(figures: Int) {
        mFigures = figures
        postInvalidate()
    }

    override fun setVerCodeMargin(margin: Int) {
        mVerCodeMargin = margin
        postInvalidate()
    }

    override fun setBottomSelectedColor(@ColorRes bottomSelectedColor: Int) {
        mBottomSelectedColor = getColor(bottomSelectedColor)
        postInvalidate()
    }

    override fun setBottomNormalColor(@ColorRes bottomNormalColor: Int) {
        mBottomSelectedColor = getColor(bottomNormalColor)
        postInvalidate()
    }

    override fun setSelectedBackgroundColor(@ColorRes selectedBackground: Int) {
        mSelectedBackgroundColor = getColor(selectedBackground)
        postInvalidate()
    }

    override fun setBottomLineHeight(bottomLineHeight: Int) {
        mBottomLineHeight = bottomLineHeight.toFloat()
        postInvalidate()
    }

    override fun setOnVerificationCodeChangedListener(listener: OnVerificationCodeChangedListener?) {
        onCodeChangedListener = listener
    }

    /**
     * 返回颜色
     */
    private fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    /**
     * dp转px
     */
    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    fun showKeyBoard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }

    companion object {
        /**
         * 获取手机屏幕的宽度
         */
        fun getScreenWidth(context: Context): Int {
            val metrics = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }
    }

    init {
        initAttrs(attrs)
        setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent)) //防止出现下划线
        initPaint()
        isFocusableInTouchMode = true
        super.addTextChangedListener(this)
    }
}