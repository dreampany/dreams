package com.dreampany.framework.misc.func

import android.os.SystemClock
import android.view.View

/**
 * Created by roman on 2019-10-26
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SafeClickListener (
    private val interval: Int = 1000,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}