package com.xxjy.common.weight.autocodeedittextview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View.OnFocusChangeListener
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import com.xxjy.common.R
@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
class VerificationCodeInput constructor(
    context: Context,
    attrs: AttributeSet?
) : ViewGroup(context, attrs) {
    private var box = 4
    private var boxWidth = 120
    private var boxHeight = 120
    private var childHPadding = 14
    private var childVPadding = 14
    private var inputType: String? = TYPE_PASSWORD
    private var boxBgFocus: Drawable? = null
    private var boxBgNormal: Drawable? = null
    private var listener: Listener? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private fun initViews() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {
                } else {
                    focus()
                    checkAndCommit()
                }
            }
        }
        val onKeyListener = OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                backFocus()
            }
            false
        }
        val onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (v is EditText) {
                setBg(v, hasFocus)
            }
        }
        for (i in 0 until box) {
            val editText = EditText(context)
            val layoutParams = LinearLayout.LayoutParams(boxWidth, boxHeight)
            layoutParams.bottomMargin = childVPadding
            layoutParams.topMargin = childVPadding
            layoutParams.leftMargin = childHPadding
            layoutParams.rightMargin = childHPadding
            layoutParams.gravity = Gravity.CENTER
            editText.setOnKeyListener(onKeyListener)
            setBg(editText, false)
            editText.setTextColor(Color.BLACK)
            editText.layoutParams = layoutParams
            editText.gravity = Gravity.CENTER
            editText.filters = arrayOf<InputFilter>(LengthFilter(1))
            if (TYPE_NUMBER == inputType) {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (TYPE_PASSWORD == inputType) {
                editText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else if (TYPE_TEXT == inputType) {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            } else if (TYPE_PHONE == inputType) {
                editText.inputType = InputType.TYPE_CLASS_PHONE
            }
            editText.id = i
            editText.setEms(1)
            editText.addTextChangedListener(textWatcher)
            editText.onFocusChangeListener = onFocusChangeListener
            addView(editText, i)
        }
    }

    private fun backFocus() {
        val count = childCount
        var editText: EditText
        for (i in count - 1 downTo 0) {
            editText = getChildAt(i) as EditText
            if (editText.text.length == 1) {
                editText.requestFocus()
                editText.setSelection(1)
                return
            }
        }
    }

    private fun focus() {
        val count = childCount
        var editText: EditText
        for (i in 0 until count) {
            editText = getChildAt(i) as EditText
            if (editText.text.length < 1) {
                editText.requestFocus()
                return
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setBg(editText: EditText, focus: Boolean) {
        if (boxBgNormal != null && !focus) {
            editText.background = boxBgNormal
        } else if (boxBgFocus != null && focus) {
            editText.background = boxBgFocus
        }
    }

    private fun checkAndCommit() {
        val stringBuilder = StringBuilder()
        var full = true
        for (i in 0 until box) {
            val editText = getChildAt(i) as EditText
            val content = editText.text.toString()
            if (content.length == 0) {
                full = false
                break
            } else {
                stringBuilder.append(content)
            }
        }
        Log.d(TAG, "checkAndCommit:$stringBuilder")
        if (full) {
            if (listener != null) {
                listener!!.onComplete(stringBuilder.toString())
                isEnabled = false
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    /**
     * 清除已经输入的text
     */
    fun clearAllText() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i) as EditText
            child.setText("")
        }
        if (childCount > 0) {
            val child = getChildAt(0) as EditText
            child.requestFocus()
        }
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    fun setTextSize(@Px textSize: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i) as EditText
            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    fun setTextColor(@ColorInt textColor: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i) as EditText
            child.setTextColor(textColor)
        }
    }

    /**
     * 设置文字颜色
     *
     * @param cursorDrawable
     */
    fun setCursorDrawable(@DrawableRes cursorDrawable: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i) as EditText
            try { //修改光标的颜色（反射）
                val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                f.isAccessible = true
                f[child] = cursorDrawable
            } catch (ignored: Exception) {
            }
        }
    }

    fun setOnCompleteListener(listener: Listener) {
        this.listener = listener
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LinearLayout.LayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(javaClass.name, "onMeasure")
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        if (count > 0) {
            val child = getChildAt(0)
            val cHeight = child.measuredHeight
            val cWidth = child.measuredWidth
            val maxH = cHeight + 2 * childVPadding
            val maxW = (cWidth + childHPadding) * box + childHPadding
            setMeasuredDimension(
                resolveSize(maxW, widthMeasureSpec),
                resolveSize(maxH, heightMeasureSpec)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(javaClass.name, "onLayout")
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.visibility = VISIBLE
            val cWidth = child.measuredWidth
            val cHeight = child.measuredHeight
            val cl = i * (cWidth + childHPadding)
            val cr = cl + cWidth
            val ct = childVPadding
            val cb = ct + cHeight
            child.layout(cl, ct, cr, cb)
        }
    }

    interface Listener {
        fun onComplete(content: String?)
    }

    companion object {
        private const val TYPE_NUMBER = "number"
        private const val TYPE_TEXT = "text"
        private const val TYPE_PASSWORD = "password"
        private const val TYPE_PHONE = "phone"
        private const val TAG = "VerificationCodeInput"
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput)
        box = a.getInt(R.styleable.VerificationCodeInput_box, 4)
        childHPadding =
            a.getDimension(R.styleable.VerificationCodeInput_child_h_padding, 0f).toInt()
        childVPadding =
            a.getDimension(R.styleable.VerificationCodeInput_child_v_padding, 0f).toInt()
        boxBgFocus = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_focus)
        boxBgNormal = a.getDrawable(R.styleable.VerificationCodeInput_box_bg_normal)
        inputType = a.getString(R.styleable.VerificationCodeInput_inputType)
        boxWidth = a.getDimension(R.styleable.VerificationCodeInput_child_width, boxWidth.toFloat())
            .toInt()
        boxHeight =
            a.getDimension(R.styleable.VerificationCodeInput_child_height, boxHeight.toFloat())
                .toInt()
        initViews()
    }
}