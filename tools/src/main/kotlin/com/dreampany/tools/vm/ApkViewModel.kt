package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.enums.ApkType
import com.dreampany.tools.data.misc.ApkMapper
import com.dreampany.tools.data.misc.ApkRequest
import com.dreampany.tools.data.model.Apk
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ApkRepository
import com.dreampany.tools.ui.model.ApkItem
import com.dreampany.tools.ui.model.UiTask
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val stateMapper: StateMapper,
    private val stateRepo: StateRepository,
    private val mapper: ApkMapper,
    private val repo: ApkRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Apk, ApkItem, UiTask<Apk>>(application, rx, ex, rm) {

    fun load(request: ApkRequest) {
        if (request.type == ApkType.DEFAULT) {
            loadMultiple(request)
        } else {
            loadSingle(request)
        }
    }

    private fun loadSingle(request: ApkRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(loadUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun loadMultiple(request: ApkRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(loadUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun loadUiItemRx(request: ApkRequest): Maybe<ApkItem> {
        return getItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemsRx(request: ApkRequest): Maybe<List<ApkItem>> {
        return repo.getItemsRx().flatMap { getUiItemsRx(it) }
    }

    private fun getItemRx(request: ApkRequest): Maybe<Apk> {
        return Maybe.create { emitter ->
        }
    }

    private fun getItemsRx(request: ApkRequest): Maybe<List<Apk>> {
        return Maybe.create { emitter ->

        }
    }

    private fun getUiItem(item: Apk): ApkItem {
        return ApkItem.getItem(item)
    }

    private fun getUiItemRx(item: Apk): Maybe<ApkItem> {
        return Maybe.create { emitter ->
            emitter.onSuccess(getUiItem(item))
        }
    }

    private fun getUiItemsRx(items: List<Apk>): Maybe<List<ApkItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }
}