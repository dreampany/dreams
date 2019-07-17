package com.dreampany.frame.data.source.repository

import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.ArrayList

/**
 * Created by roman on 2019-07-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class RepositoryKt<K, T>(val rx: RxMapper, val rm: ResponseMapper) {

    val subject: PublishSubject<Response<T>> = PublishSubject.create()
    val subjects: PublishSubject<Response<List<T>>> = PublishSubject.create()

    @SafeVarargs
    protected fun concatSingleFirstRx(vararg sources: Maybe<T>): Maybe<T> {
        return Maybe.create<T> { emitter ->
            var error: Exception? = null
            var item: T? = null

            for (source in sources) {
                try {
                    item = source.blockingGet()
                } catch (ex: Exception) {
                    error = ex
                }

                if (item != null) {
                    break
                }
            }
            if (item == null) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    item?.let {
                        emitter.onSuccess(it)
                    }

                }
            }
        }
    }

    @SafeVarargs
    protected fun concatFirstOfStringRx(vararg sources: Maybe<List<String>>): Maybe<List<String>> {
        return Maybe.create { emitter ->
            var error: Throwable? = null
            var items: List<String>? = null

            for (source in sources) {
                try {
                    items = source.blockingGet()
                    Timber.v("Concat %d", items!!.size)
                } catch (ex: Throwable) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    break
                }
            }
            if (DataUtil.isEmpty(items)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isDisposed) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    items?.let {
                        emitter.onSuccess(it)
                    }
                }
            }
        }
    }

    @SafeVarargs
    protected fun concatFirstRx(vararg sources: Maybe<List<T>>): Maybe<List<T>> {
        return Maybe.create<List<T>> { emitter ->
            var error: Throwable? = null
            var items: List<T>? = null

            for (source in sources) {
                try {
                    items = source.blockingGet()
                    Timber.v("Concat %d", items!!.size)
                } catch (ex: Throwable) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    break
                }
            }
            if (DataUtil.isEmpty(items)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isDisposed) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    items?.let {
                        emitter.onSuccess(it)
                    }
                }
            }
        }
    }

    @SafeVarargs
    protected fun concatFirstRx(vararg sources: Flowable<List<T>>): Flowable<List<T>> {
        return Flowable.create<List<T>>({ emitter ->
            var error: Throwable? = null
            var items: List<T>? = null

            for (source in sources) {
                try {
                    items = source.blockingFirst()
                } catch (ex: Exception) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    break
                }
            }
            if (DataUtil.isEmpty(items)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isCancelled) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    items?.let {
                        //emitter.onSuccess(it)
                    }
                }
            }

        }, BackpressureStrategy.BUFFER)

    }

    @SafeVarargs
    protected fun concatLastRx(vararg sources: Flowable<List<T>>): Flowable<List<T>> {
        return Flowable.create<List<T>>({ emitter ->
            var error: Throwable? = null
            var result: List<T>? = null

            for (source in sources) {
                var items: List<T>? = null
                try {
                    items = source.blockingFirst()
                } catch (ex: Exception) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    result = items
                }
            }
            if (DataUtil.isEmpty(result)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isCancelled) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    result?.let {
                        emitter.onNext(it)
                    }
                }
            }

        }, BackpressureStrategy.BUFFER)
    }

    @SafeVarargs
    protected fun concatSingleLastRx(vararg sources: Maybe<T>): Maybe<T> {
        return Maybe.create<T> { emitter ->
            var error: Throwable? = null
            var result: T? = null

            for (source in sources) {
                var item: T? = null
                try {
                    item = source.blockingGet()
                } catch (ex: Exception) {
                    error = ex
                }

                if (item != null) {
                    result = item
                }
            }
            if (result == null) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }
            if (!emitter.isDisposed()) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    result?.let {
                        emitter.onSuccess(it)
                    }
                }
            }
        }
    }

    @SafeVarargs
    protected fun concatLastRx(vararg sources: Maybe<List<T>>): Maybe<List<T>> {
        return Maybe.create<List<T>> { emitter ->
            var error: Throwable? = null
            var result: List<T>? = null

            for (source in sources) {
                var items: List<T>? = null
                try {
                    items = source.blockingGet()
                } catch (ex: Exception) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    result = items
                }
            }
            if (DataUtil.isEmpty(result)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isDisposed) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    result?.let {
                        emitter.onSuccess(it)
                    }
                }
            }
        }
    }

    @SafeVarargs
    protected fun collectRx(target: Int, vararg sources: Maybe<List<T>>): Maybe<List<T>> {
        return Maybe.create<List<T>> { emitter ->
            var error: Throwable? = null
            var result: MutableList<T>? = null

            for (source in sources) {
                var items: List<T>? = null
                try {
                    items = source.blockingGet()
                } catch (ex: Exception) {
                    error = ex
                }

                if (!DataUtil.isEmpty(items)) {
                    if (result == null) {
                        result = ArrayList<T>()
                    }
                    result.addAll(items!!)
                    if (result.size >= target) {
                        break
                    }
                }
            }
            if (DataUtil.isEmpty(result)) {
                if (error == null) {
                    error = EmptyException()
                }
            } else {
                error = null
            }

            if (!emitter.isDisposed) {
                if (error != null) {
                    emitter.onError(error)
                } else {
                    result?.let {
                        emitter.onSuccess(it)
                    }
                }
            }
        }
    }

    protected fun contactSingleSuccess(source: Maybe<T>, onSuccess: Consumer<T>?): Maybe<T> {
        var maybe = source
            .filter { v -> !DataUtil.isEmpty(v) }
        if (onSuccess != null) {
            maybe = maybe.doOnSuccess(onSuccess)
        }
        return maybe
    }

    protected fun contactSuccess(
        source: Maybe<List<T>>,
        onSuccess: Consumer<List<T>>?
    ): Maybe<List<T>> {
        var maybe = source
            .filter { vs -> !DataUtil.isEmpty(vs) }
        if (onSuccess != null) {
            maybe = maybe.doOnSuccess(onSuccess)
        }
        return maybe
    }
}