package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Extra
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.exceptions.ExtraException
import com.dreampany.framework.misc.exceptions.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.AppMapper
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.source.pref.LockPref
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.AppRepository
import com.dreampany.tools.ui.model.AppItem
import com.dreampany.tools.ui.request.AppRequest
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
    private val lockPref: LockPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: AppMapper,
    private val repo: AppRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>,
    @Extra private val extras: SmartMap<String, Boolean>
) : BaseViewModel<App, AppItem, UiTask<App>>(application, rx, ex, rm) {

    fun request(request: AppRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: AppRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postResult(
                    state = request.state,
                    action = request.action,
                    data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailures(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addSingleSubscription(disposable)
    }

    private fun requestMultiple(request: AppRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(loadUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postResult(
                    state = request.state,
                    action = request.action,
                    data = result
                )
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailures(
                    state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemRx(request: AppRequest): Maybe<AppItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun loadUiItemsRx(request: AppRequest): Maybe<List<AppItem>> {
        return repo.getItemsRx().flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemRx(request: AppRequest): Maybe<App> {
        request.id?.run {
            mapper.getItem(this)?.run {
                return Maybe.create { emitter ->
                    emitter.onSuccess(this)
                }
            }
        }
        return Maybe.empty()
    }

    private fun getItemsRx(request: AppRequest): Maybe<List<App>> {
        return Maybe.create { emitter ->

        }
    }

    private fun getUiItem(request: AppRequest, item: App): AppItem {
        var uiItem: AppItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = AppItem.getItem(item, request.lockStatus)
            mapper.putUiItem(item.id, uiItem)
        } else {
            if (request.lockStatus) {
                uiItem.layoutId = R.layout.item_lock_app
            } else {
                uiItem.layoutId = R.layout.item_app
            }
        }
        uiItem.item = item
        uiItem.lockStatus = request.lockStatus
        if (request.lockStatus) {
            adjustLockStatus(item, uiItem)
        }
        return uiItem
    }

    private fun getUiItemRx(request: AppRequest, item: App): Maybe<AppItem> {
        return Maybe.create { emitter ->
            if (request.action == Action.LOCK)
                toggleLock(item.id)

            val uiItem = getUiItem(request, item)
            if (emitter.isDisposed) return@create
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(request: AppRequest, items: List<App>): Maybe<List<AppItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun adjustLockStatus(item: App, uiItem: AppItem) {
        uiItem.locked = isLocked(item)
    }

    private fun isLocked(item: App): Boolean {
        if (!extras.contains(item.id)) {
            val locks = lockPref.getLockedPackages()
            val locked = locks.contains(item.id)
            extras.put(item.id, locked)
        }
        return extras.get(item.id)
    }

    private fun toggleLock(id: String): Boolean {
        val locks = lockPref.getLockedPackages()
        val locked = locks.contains(id)
        if (locked) {
            lockPref.removeLockedPackage(id)
        } else {
            lockPref.addLockedPackage(id)
        }
        extras.put(id, locked.not())
        return extras.get(id)
    }
}