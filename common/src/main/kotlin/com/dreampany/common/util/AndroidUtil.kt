package com.dreampany.common.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AndroidUtil private constructor() {

    companion object {
        fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        fun hasMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        fun hasOreo(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        fun hasNougat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        fun isAlive(activity: Activity?): Boolean {
            if (activity == null) {
                return false
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                !(activity.isFinishing || activity.isDestroyed)
            } else !activity.isFinishing
        }

        fun isAlive(fragment: Fragment?): Boolean {
            return fragment?.isVisible ?: false
        }

        fun <T : Activity> openActivity(source: T, target: Class<*>) {
            openActivity(source, target, false)
        }

        fun <T : Activity> openActivity(source: T?, target: Class<*>, finish: Boolean) {
            if (source != null) {
                source.startActivity(Intent(source, target))
                if (finish) {
                    source.finish()
                }
            }
        }
    }
}