package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.CoinRepository
import com.dreampany.tools.ui.misc.CoinRequest
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.ui.model.CoinItem
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.model.StationItem
import com.dreampany.tools.util.CurrencyFormatter
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val cryptoPref: CryptoPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: CoinMapper,
    private val repo: CoinRepository,
    private val formatter: CurrencyFormatter,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Coin, CoinItem, UiTask<Coin>>(application, rx, ex, rm), NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun getInfos(item: CoinItem): List<CoinItem> {
        val result = arrayListOf<CoinItem>()
        result.add(CoinItem.getInfoItem(item.currency, item.item, formatter))
        result.add(CoinItem.getQuoteItem(item.currency, item.item, formatter))
        return result
    }

    fun request(request: CoinRequest) {
        if (request.single) {
            requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestSingle(request: CoinRequest) {
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

    private fun requestMultiple(request: CoinRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
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

    private fun requestUiItemRx(request: CoinRequest): Maybe<CoinItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestItemRx(request: CoinRequest): Maybe<Coin> {
        request.id?.run {
            return repo.getItemRx(request.currency, this)
        }
        return Maybe.empty()
    }

    private fun requestUiItemsRx(request: CoinRequest): Maybe<List<CoinItem>> {
        if (request.action == Action.PAGINATE) {
            return repo.getItemsRx(
                request.currency,
                request.sort,
                request.order,
                request.start,
                request.limit
            ).flatMap { getUiItemsRx(request, it) }
        }
        if (request.action == Action.UPDATE) {
            if (request.single) {

            } else {
                return repo.getItemsRx(
                    request.currency,
                    request.ids!!
                ).flatMap { getUiItemsRx(request, it) }
            }
        }
        return repo.getItemsRx().flatMap { getUiItemsRx(request, it) }
    }

    private fun getUiItemRx(request: CoinRequest, item: Coin): Maybe<CoinItem> {
        Timber.v("%s", item.toString())
        return Maybe.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            if (request.action == Action.FAVORITE) {
                //toggleFavorite(item.id)
            }
            val uiItem = getUiItem(request, item)
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItemsRx(request: CoinRequest, items: List<Coin>): Maybe<List<CoinItem>> {
        Timber.v("For UI items %d", items.size)
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: CoinRequest, item: Coin): CoinItem {
        var uiItem: CoinItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = CoinItem.getItem(request.currency, item, formatter)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        adjustFavorite(item, uiItem)
        return uiItem
    }

    private fun adjustFavorite(item: Coin, uiItem: CoinItem) {
        uiItem.favorite = isFavorite(item)
    }

    private fun isFavorite(item: Coin): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(item.id)) {
            val favorite = hasStore(item.id, Type.COIN, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", item.id, favorite)
            favorites.put(item.id, favorite)
        }
        return favorites.get(item.id)
    }

    private fun hasStore(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return storeRepo.isExists(id, type, subtype, state)
    }

    private fun putStore(id: String, type: Type, subtype: Subtype, state: State): Long {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.putItem(store)
    }

    private fun removeStore(id: String, type: Type, subtype: Subtype, state: State): Int {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.delete(store)
    }
}