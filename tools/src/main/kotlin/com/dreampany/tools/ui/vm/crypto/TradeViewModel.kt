package com.dreampany.tools.ui.vm.crypto

import android.app.Application
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.misc.exceptions.ExtraException
import com.dreampany.framework.misc.exceptions.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.crypto.TradeMapper
import com.dreampany.tools.data.model.crypto.Trade
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.crypto.TradeRepository
import com.dreampany.tools.ui.model.crypto.TradeItem
import com.dreampany.tools.ui.request.crypto.TradeRequest
import com.dreampany.tools.util.CurrencyFormatter
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TradeViewModel
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
    private val mapper: TradeMapper,
    private val repo: TradeRepository,
    private val formatter: CurrencyFormatter,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Trade, TradeItem, UiTask<Trade>>(application, rx, ex, rm),
    NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: TradeRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: TradeRequest) {
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

    private fun requestUiItemsRx(request: TradeRequest): Maybe<List<TradeItem>> {
        return repo.getTradesRx(request.extraParams, request.fromSymbol, request.limit)
            .flatMap { getUiItemsRx(request, it) }
    }

    private fun getUiItemsRx(request: TradeRequest, items: List<Trade>): Maybe<List<TradeItem>> {
        Timber.v("For UI items %d", items.size)
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: TradeRequest, item: Trade): TradeItem {
        var uiItem: TradeItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = TradeItem.getItem(item, formatter)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        return uiItem
    }
}