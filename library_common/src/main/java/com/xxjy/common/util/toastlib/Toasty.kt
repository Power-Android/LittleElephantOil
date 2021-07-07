package com.xxjy.common.util.toastlib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.xxjy.common.R

/**
 * This file is part of Toasty.
 *
 *
 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
@SuppressLint("InflateParams", "StaticFieldLeak")
object Toasty {
    @ColorInt
    private var DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private var ERROR_COLOR = Color.parseColor("#D50000")

    @ColorInt
    private var INFO_COLOR = Color.parseColor("#3F51B5")

    @ColorInt
    private var SUCCESS_COLOR = Color.parseColor("#388E3C")

    @ColorInt
    private var WARNING_COLOR = Color.parseColor("#FFA900")

    @ColorInt
    private val NORMAL_COLOR = Color.parseColor("#353A3E")
    private val LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
    private var currentTypeface = LOADED_TOAST_TYPEFACE
    private var textSize = 16 // in SP
    private var tintIcon = true
    private var currentToast //全局维护一个toast 即可
            : Toast? = null
    private var toastLayout //全局维护一个toastLayout 即可
            : View? = null
    private var toastIcon: ImageView? = null
    private var toastTextView: TextView? = null
    @CheckResult
    fun normal(context: Context, message: CharSequence): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: CharSequence, icon: Drawable?): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    fun normal(context: Context, message: CharSequence, duration: Int): Toast? {
        return normal(context, message, duration, null, false)
    }

    @CheckResult
    fun normal(
        context: Context, message: CharSequence, duration: Int,
        icon: Drawable?
    ): Toast? {
        return normal(context, message, duration, icon, true)
    }

    @CheckResult
    fun normal(
        context: Context, message: CharSequence, duration: Int,
        icon: Drawable?, withIcon: Boolean
    ): Toast? {
        return custom(context, message, icon, NORMAL_COLOR, duration, withIcon, true)
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence): Toast? {
        return warning(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence, duration: Int): Toast? {
        return warning(context, message, duration, true)
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context, message, ToastyUtils.getDrawable(context, R.drawable.toast_icon_warning),
            WARNING_COLOR, duration, withIcon, true
        )
    }

    @CheckResult
    fun info(context: Context, message: CharSequence): Toast? {
        return info(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun info(context: Context, message: CharSequence, duration: Int): Toast? {
        return info(context, message, duration, true)
    }

    @CheckResult
    fun info(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context, message, ToastyUtils.getDrawable(context, R.drawable.toast_icon_info),
            INFO_COLOR, duration, withIcon, true
        )
    }

    @CheckResult
    fun success(context: Context, message: CharSequence): Toast? {
        return success(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun success(context: Context, message: CharSequence, duration: Int): Toast? {
        return success(context, message, duration, true)
    }

    @CheckResult
    fun success(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context, message, ToastyUtils.getDrawable(context, R.drawable.toast_icon_success),
            SUCCESS_COLOR, duration, withIcon, true
        )
    }

    @CheckResult
    fun error(context: Context, message: CharSequence): Toast? {
        return error(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun error(context: Context, message: CharSequence, duration: Int): Toast? {
        return error(context, message, duration, true)
    }

    @CheckResult
    fun error(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context, message, ToastyUtils.getDrawable(context, R.drawable.toast_icon_error),
            ERROR_COLOR, duration, withIcon, true
        )
    }

    @CheckResult
    fun custom(
        context: Context, message: CharSequence, icon: Drawable?,
        duration: Int, withIcon: Boolean
    ): Toast? {
        return custom(context, message, icon, -1, duration, withIcon, false)
    }

    @CheckResult
    fun custom(
        context: Context, message: CharSequence, @DrawableRes iconRes: Int,
        @ColorInt tintColor: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast? {
        return custom(
            context, message, ToastyUtils.getDrawable(context, iconRes),
            tintColor, duration, withIcon, shouldTint
        )
    }

    /**
     * 创建一个 居中显示的 toast
     *
     * @param context
     * @param message    需要展示的消息
     * @param icon       需要展示的drawable图片
     * @param tintColor  toast的背景颜色
     * @param duration   展示时长, short , long 两种
     * @param withIcon   是否展示icon,一般有icon 会展示
     * @param shouldTint 是否需要自定义背景颜色
     * @return
     */
    @CheckResult
    fun custom(
        context: Context, message: CharSequence, icon: Drawable?,
        @ColorInt tintColor: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast? {
        // todo 这一步是后期处理加上的, 目的是 不让背景修改颜色 至此: shouldTint 和 tintColor 两个字段全部无效了
        var icon = icon
        var shouldTint = shouldTint
        shouldTint = false
        if (currentToast == null) {
            currentToast = Toast(context)
        }
        if (toastLayout == null) {
            toastLayout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
            toastIcon = toastLayout?.findViewById<View>(R.id.toast_icon) as ImageView
            toastTextView = toastLayout?.findViewById<View>(R.id.toast_text) as TextView
        }
        var drawableFrame: Drawable

//        if (shouldTint)
//            drawableFrame = ToastyUtils.tint9PatchDrawableFrame(context, tintColor);
//        else
//            drawableFrame = ToastyUtils.getDrawable(context, R.drawable.toast_frame_range);
//        ToastyUtils.setBackground(toastLayout, drawableFrame);
        if (withIcon) {
            requireNotNull(icon) { "如果设置 withIcon 为 true,则 icon 不可为空" }
            if (tintIcon) //tintIcon是用来在全局配置是否展示 icon 的.
                icon = ToastyUtils.tintIcon(icon, DEFAULT_TEXT_COLOR)
            toastIcon!!.visibility = View.VISIBLE
            ToastyUtils.setBackground(toastIcon!!, icon)
        } else {
            toastIcon!!.visibility = View.GONE
        }
        toastTextView!!.setTextColor(DEFAULT_TEXT_COLOR)
        toastTextView!!.text = message
        toastTextView!!.setTypeface(currentTypeface)
        toastTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        currentToast!!.setView(toastLayout)
        currentToast!!.duration = duration
        currentToast!!.setGravity(Gravity.CENTER, 0, -150)
        return currentToast
    }

    class Config private constructor() {
        @ColorInt
        private var DEFAULT_TEXT_COLOR = Toasty.DEFAULT_TEXT_COLOR

        @ColorInt
        private var ERROR_COLOR = Toasty.ERROR_COLOR

        @ColorInt
        private var INFO_COLOR = Toasty.INFO_COLOR

        @ColorInt
        private var SUCCESS_COLOR = Toasty.SUCCESS_COLOR

        @ColorInt
        private var WARNING_COLOR = Toasty.WARNING_COLOR
        private var typeface = currentTypeface
        private var textSize = Toasty.textSize
        private var tintIcon = Toasty.tintIcon
        @CheckResult
        fun setTextColor(@ColorInt textColor: Int): Config {
            DEFAULT_TEXT_COLOR = textColor
            return this
        }

        @CheckResult
        fun setErrorColor(@ColorInt errorColor: Int): Config {
            ERROR_COLOR = errorColor
            return this
        }

        @CheckResult
        fun setInfoColor(@ColorInt infoColor: Int): Config {
            INFO_COLOR = infoColor
            return this
        }

        @CheckResult
        fun setSuccessColor(@ColorInt successColor: Int): Config {
            SUCCESS_COLOR = successColor
            return this
        }

        @CheckResult
        fun setWarningColor(@ColorInt warningColor: Int): Config {
            WARNING_COLOR = warningColor
            return this
        }

        @CheckResult
        fun setToastTypeface(typeface: Typeface): Config {
            this.typeface = typeface
            return this
        }

        @CheckResult
        fun setTextSize(sizeInSp: Int): Config {
            this.textSize = sizeInSp
            return this
        }

        @CheckResult
        fun tintIcon(tintIcon: Boolean): Config {
            this.tintIcon = tintIcon
            return this
        }

        fun apply() {
            Toasty.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR
            Toasty.ERROR_COLOR = ERROR_COLOR
            Toasty.INFO_COLOR = INFO_COLOR
            Toasty.SUCCESS_COLOR = SUCCESS_COLOR
            Toasty.WARNING_COLOR = WARNING_COLOR
            currentTypeface = typeface
            Toasty.textSize = textSize
            Toasty.tintIcon = tintIcon
        }

        companion object {
            @get:CheckResult
            val instance: Config
                get() = Config()

            fun reset() {
                DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")
                ERROR_COLOR = Color.parseColor("#D50000")
                INFO_COLOR = Color.parseColor("#3F51B5")
                SUCCESS_COLOR = Color.parseColor("#388E3C")
                WARNING_COLOR = Color.parseColor("#FFA900")
                currentTypeface = LOADED_TOAST_TYPEFACE
                textSize = 16
                tintIcon = true
            }
        }
    }
}