package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.StationMapper
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.RadioPref
import com.dreampany.tools.data.source.repository.StationRepository
import com.dreampany.tools.ui.misc.StationRequest
import com.dreampany.tools.ui.model.StationItem
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val radioPref: RadioPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: StationMapper,
    private val repo: StationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Station, StationItem, UiTask<Station>>(application, rx, ex, rm),
    NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: StationRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: StationRequest) {
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
                postFailures(request.state, request.action,MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemsRx(request: StationRequest): Maybe<List<StationItem>> {
/*        if (request.action == Action.FAVORITE) {
            return storeRepo
                .requestItemsRx(Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }*/
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: StationRequest): Maybe<List<Station>> {
        when (request.state) {
            State.LOCAL -> {
                return repo.getItemsOfCountryRx(request.countryCode!!, request.limit)
            }
            State.TRENDS -> {
                return repo.getItemsOfTrendsRx(request.limit)
            }
            State.POPULAR -> {
                return repo.getItemsOfPopularRx(request.limit)
            }
        }
        return repo.getItemsRx();
    }

    private fun getUiItemsRx(
        request: StationRequest,
        items: List<Station>
    ): Maybe<List<StationItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: StationRequest, item: Station): StationItem {
        var uiItem: StationItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = StationItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        return uiItem
    }

    private fun adjustFavorite(item: Station, uiItem: StationItem) {
        uiItem.favorite = isFavorite(item)
    }

    private fun isFavorite(item: Station): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(item.id)) {
            val favorite = hasStore(item.id, Type.STATION, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", item.id, favorite)
            favorites.put(item.id, favorite)
        }
        return favorites.get(item.id)
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
        if (favorite) {
            removeStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, false)
        } else {
            putStore(id, Type.NOTE, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, true)
        }
        return favorites.get(id)
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