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
import com.dreampany.tools.data.enums.FeatureType
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.data.misc.FeatureRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.model.FeatureItem
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
class FeatureViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val stateMapper: StateMapper,
    private val stateRepo: StateRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Feature, FeatureItem, UiTask<Feature>>(application, rx, ex, rm) {

    fun load(request: FeatureRequest) {
        if (request.action == Action.GET) {
            if (request.single) {
                loadSingle(request)
            } else {
                loadMultiple(request)
            }
            return
        }
    }

    private fun loadSingle(request: FeatureRequest) {
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

    private fun loadMultiple(request: FeatureRequest) {
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

    private fun loadUiItemRx(request: FeatureRequest): Maybe<FeatureItem> {
        return getItemRx(request).flatMap { getUiItemRx(it) }
    }

    private fun loadUiItemsRx(request: FeatureRequest): Maybe<List<FeatureItem>> {
        return getItemsRx(request).flatMap { getUiItemsRx(it) }
    }

    private fun getItemRx(request: FeatureRequest): Maybe<Feature> {
        return Maybe.create { emitter ->
            val item = Feature(request.type.name, request.type)
            emitter.onSuccess(item)
        }
    }

    private fun getItemsRx(request: FeatureRequest): Maybe<List<Feature>> {
        return Maybe.create { emitter ->
            val items = mutableListOf<Feature>()
            items.add(Feature(FeatureType.APK.name, FeatureType.APK))
            items.add(Feature(FeatureType.SCAN.name, FeatureType.SCAN))
            items.add(Feature(FeatureType.NOTE.name, FeatureType.NOTE))
            items.add(Feature(FeatureType.WORD.name, FeatureType.WORD))
            items.add(Feature(FeatureType.HISTORY.name, FeatureType.HISTORY))
            emitter.onSuccess(items)
        }
    }

    private fun getUiItem(item: Feature): FeatureItem {
        return FeatureItem.getItem(item)
    }

    private fun getUiItemRx(item: Feature): Maybe<FeatureItem> {
        return Maybe.create { emitter ->
            emitter.onSuccess(getUiItem(item))
        }
    }

    private fun getUiItemsRx(items: List<Feature>): Maybe<List<FeatureItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }
}