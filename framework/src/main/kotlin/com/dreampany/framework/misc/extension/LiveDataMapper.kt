package com.dreampany.framework.misc.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Created by Hawladar Roman on 5/31/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}