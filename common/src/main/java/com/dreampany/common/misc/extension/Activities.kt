package com.dreampany.common.misc.extension

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by roman on 17/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun <T : Any> Activity?.open(target: KClass<T>, finishCurrent: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finishCurrent) {
            finish()
        }
    }
}
