package com.xxjy.common.util.toastlib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

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
internal object ToastyUtils {
    fun tintIcon(drawable: Drawable, @ColorInt tintColor: Int): Drawable {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun setBackground(view: View, drawable: Drawable?) {
        view.background =
            drawable
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) context.getDrawable(id) else context.resources.getDrawable(
            id
        )
    }
}