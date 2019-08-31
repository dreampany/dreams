package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.data.misc.FeatureRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.model.FeatureItem
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.TextUtil
import com.dreampany.tools.R
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
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
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
            items.add(Feature(type = Type.APP, title = TextUtil.getString(getApplication(), R.string.title_feature_app)))
            items.add(Feature(type = Type.WORD, title = TextUtil.getString(getApplication(), R.string.title_feature_word)))
            items.add(Feature(type = Type.NOTE, title = TextUtil.getString(getApplication(), R.string.title_feature_note)))
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