package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.ui.misc.FeatureRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.model.FeatureItem
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.misc.Constants
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
    ex: AppExecutor,
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
                    postProgress(request.state, request.action, true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postResult(request.state, Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postFailures(request.state, request.action, MultiException(error, ExtraException()))
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
                    postProgress(request.state, request.action, true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postResult(request.state, Action.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postFailures(request.state, request.action, MultiException(error, ExtraException()))
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
            if (emitter.isDisposed) return@create
            emitter.onSuccess(item)
        }
    }

    private fun getItemsRx(request: FeatureRequest): Maybe<List<Feature>> {
        return Maybe.create { emitter ->
            val pairs = arrayListOf<Triple<Type, Subtype, Int>>()
            pairs.add(Triple(Type.APP, Subtype.DEFAULT, R.string.title_feature_app))
            pairs.add(Triple(Type.NOTE, Subtype.DEFAULT, R.string.title_feature_note))
            pairs.add(Triple(Type.WORD, Subtype.DEFAULT, R.string.title_feature_word))
            pairs.add(Triple(Type.RADIO, Subtype.DEFAULT, R.string.title_feature_radio))
            pairs.add(Triple(Type.VPN, Subtype.DEFAULT, R.string.title_feature_vpn))
            //pairs.add(Triple(Type.CALL, Subtype.BLOCK, R.string.title_feature_call_block))

            val result = arrayListOf<Feature>()
            pairs.forEach { pair ->
                result.add(
                    Feature(
                        type = pair.first,
                        subtype = pair.second,
                        title = TextUtil.getString(getApplication(), pair.third)
                    )
                )
            }
            if (emitter.isDisposed) return@create
            emitter.onSuccess(result)
        }
    }

    private fun getUiItem(item: Feature): FeatureItem {
        return FeatureItem.getItem(item).apply {
            order = Constants.Order.getOrder(this.item.type)
        }
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