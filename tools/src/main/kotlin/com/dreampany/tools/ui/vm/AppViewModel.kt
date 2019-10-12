package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.AppMapper
import com.dreampany.tools.ui.misc.AppRequest
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.AppRepository
import com.dreampany.tools.ui.model.AppItem
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: AppMapper,
    private val repo: AppRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<App, AppItem, UiTask<App>>(application, rx, ex, rm) {

    fun request(request: AppRequest) {
        if (request.single) {
            loadSingle(request)
        } else {
            loadMultiple(request)
        }
    }

    private fun loadSingle(request: AppRequest) {
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
                postResult(request.state, Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun loadMultiple(request: AppRequest) {
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
                postResult(request.state, Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun loadUiItemRx(request: AppRequest): Maybe<AppItem> {
        return getItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemsRx(request: AppRequest): Maybe<List<AppItem>> {
        return repo.getItemsRx().flatMap { getUiItemsRx(it) }
    }

    private fun getItemRx(request: AppRequest): Maybe<App> {
        return Maybe.create { emitter ->
        }
    }

    private fun getItemsRx(request: AppRequest): Maybe<List<App>> {
        return Maybe.create { emitter ->

        }
    }

    private fun getUiItem(item: App): AppItem {
        return AppItem.getItem(item)
    }

    private fun getUiItemRx(item: App): Maybe<AppItem> {
        return Maybe.create { emitter ->
            emitter.onSuccess(getUiItem(item))
        }
    }

    private fun getUiItemsRx(items: List<App>): Maybe<List<AppItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }
}