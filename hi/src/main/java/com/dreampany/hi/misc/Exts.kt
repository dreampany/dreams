package com.dreampany.hi

import android.app.Activity
import android.content.Intent
import android.os.Looper
import com.google.common.io.BaseEncoding
import kotlin.reflect.KClass

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
fun Runnable.isOnUiThread(): Boolean =
    Thread.currentThread() === Looper.getMainLooper().getThread()

fun <T : Any> Activity?.open(target: KClass<T>, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finish) {
            finish()
        }
    }
}

val String.encodeBase64: String
    get() = BaseEncoding.base64().encode(this.toByteArray(Charsets.UTF_8))

val String.decodeBase64: String
    get() = BaseEncoding.base64().decode(this).toString(Charsets.UTF_8)