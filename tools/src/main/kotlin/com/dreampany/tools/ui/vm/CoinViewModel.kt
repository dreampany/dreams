package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.CoinMapper
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.source.pref.CryptoPref
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.CoinRepository
import com.dreampany.tools.ui.misc.CoinRequest
import com.dreampany.tools.ui.model.CoinItem
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
    private val coinPref: CryptoPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: CoinMapper,
    private val repo: CoinRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Coin, CoinItem, UiTask<Coin>>(application, rx, ex, rm), NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: CoinRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: CoinRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(request.state, request.action,true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(request.state, request.action,false)
                }
                postResult(request.state, request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(request.state, request.action,false)
                }
                postFailures(request.state, request.action, MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemsRx(request: CoinRequest): Maybe<List<CoinItem>> {
        if (request.state == State.PAGINATED) {
            return repo.getItemsRx(
                request.currency!!,
                request.sort!!,
                request.order!!,
                request.start,
                request.limit
                ).flatMap { getUiItemsRx(request, it) }
        }
        return repo.getItemsRx().flatMap { getUiItemsRx(request, it) }
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
            uiItem = CoinItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        return uiItem
    }

}