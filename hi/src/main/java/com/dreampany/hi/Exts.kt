package com.dreampany.hi

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */

fun <T : Any> Activity?.open(target: KClass<T>, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finish) {
            finish()
        }
    }
}