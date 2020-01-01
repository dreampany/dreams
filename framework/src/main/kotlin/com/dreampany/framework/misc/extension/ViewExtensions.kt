package com.dreampany.framework.misc.extension

import android.view.View
import com.dreampany.framework.misc.func.SafeClickListener

/**
 * Created by roman on 2019-10-26
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun View.setOnSafeClickListener(
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

fun View.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener(interval, {v->
        onSafeClick(v)
    }))
}