package com.dreampany.frame.misc

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by Hawladar Roman on 5/31/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
// Extensions for RxJava2

/**
 * Safely dispose = if not null and not already disposed
 */
fun Disposable?.safeDispose() {
    if (this?.isDisposed == false) {
        dispose()
    }
}

/**
 * Overloaded operator to allow adding [Disposable] to [CompositeDisposable] via + sign
 */
operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable {
    this.add(disposable)
    return this
}