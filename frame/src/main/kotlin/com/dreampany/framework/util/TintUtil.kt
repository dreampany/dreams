package com.dreampany.framework.util

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat

/**
 * Created by roman on 2019-09-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TintUtil {
    companion object {
        fun tintImageView(view : AppCompatImageView, @ColorRes colorRes: Int) {
            ImageViewCompat.setImageTintList(
                view, ColorStateList.valueOf(ColorUtil.getColor(view.context, colorRes)));
        }
    }
}