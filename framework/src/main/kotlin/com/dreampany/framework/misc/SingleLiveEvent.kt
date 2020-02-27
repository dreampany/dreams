package com.dreampany.framework.misc

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.annotation.MainThread
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by roman on 2019-07-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SingleLiveEvent<T>: MutableLiveData<T>() {

    private val TAG = SingleLiveEvent::class.java.simpleName
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Timber.tag(TAG).i("Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }
}