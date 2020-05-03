package com.dreampany.framework.misc.extension

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Runnable.isOnUiThread(): Boolean =
    Thread.currentThread() === Looper.getMainLooper().getThread()

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

