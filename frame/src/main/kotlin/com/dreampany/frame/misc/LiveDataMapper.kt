package com.dreampany.frame.misc

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer


/**
 * Created by Hawladar Roman on 5/31/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}