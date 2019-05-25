package com.dreampany.network.misc

import androidx.lifecycle.MutableLiveData
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/26/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class RxMapper @Inject constructor(var facade: RxFacade) {

    fun <T> toLiveData(subject: AsyncSubject<T>, disposables: CompositeDisposable): MutableLiveData<T> {
        val data = MutableLiveData<T>()
        val disposable = subject.subscribe({ data.setValue(it) })
        disposables.add(disposable)
        return data
    }

    fun <T> toLiveData(subject: ReplaySubject<T>, disposables: CompositeDisposable): MutableLiveData<T> {
        val data = MutableLiveData<T>()
        val disposable = subject.subscribe({ data.setValue(it) })
        disposables.add(disposable)
        return data
    }

    fun <T> toLiveData(subject: PublishSubject<T>, disposables: CompositeDisposable): MutableLiveData<T> {
        val data = MutableLiveData<T>()
        val disposable = subject.subscribe({ data.setValue(it) })
        disposables.add(disposable)
        return data
    }


    fun backToMain(completable: Completable): Completable {
        return completable.subscribeOn(facade.io()).observeOn(facade.ui())
    }

    fun <T> backToMain(single: Single<T>): Single<T> {
        return single.subscribeOn(facade.io()).observeOn(facade.ui())
    }

    fun <T> backToMain(maybe: Maybe<T>): Maybe<T> {
        return maybe.subscribeOn(facade.io()).observeOn(facade.ui())
    }

    fun <T> backToBack(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(facade.io()).observeOn(facade.compute())
    }

    fun <T> backToMain(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(facade.io()).observeOn(facade.ui())
    }

    fun <T> backToMain(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(facade.io()).observeOn(facade.ui())
    }

    fun back(completable: Completable): Completable {
        return completable.subscribeOn(facade.io())
    }

    fun <T> back(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(facade.io())
    }

    fun <T> back(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(facade.io())
    }

    fun <T> compute(completable: Completable): Completable {
        return completable.subscribeOn(facade.compute())
    }

    fun <T> compute(single: Single<T>): Single<T> {
        return single.subscribeOn(facade.compute())
    }

    fun <T> compute(flowable: Flowable<T>): Flowable<T> {
        return flowable.subscribeOn(facade.compute())
    }

    fun io(): Scheduler {
        return facade.io()
    }

    fun compute(): Scheduler {
        return facade.compute()
    }

    fun ui(): Scheduler {
        return facade.ui()
    }
}
