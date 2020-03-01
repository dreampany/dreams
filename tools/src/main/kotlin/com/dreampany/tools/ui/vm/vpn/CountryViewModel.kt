package com.dreampany.tools.ui.vm.vpn

import android.app.Application
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.model.Country
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.injector.annote.Favorite
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exceptions.ExtraException
import com.dreampany.framework.misc.exceptions.MultiException
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.CountryMapper
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ServerRepository
import com.dreampany.tools.ui.request.CountryRequest
import com.dreampany.tools.ui.model.CountryItem
import com.google.common.collect.Maps
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2020-01-04
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CountryViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: CountryMapper,
    private val serverMapper: ServerMapper,
    private val repo: ServerRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Country, CountryItem, UiTask<Country>>(application, rx, ex, rm),
    NetworkManager.Callback {

    private lateinit var uiCallback: SmartAdapter.Callback<CountryItem>

    init {
        network.observe(this, checkInternet = true)
    }

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: CountryRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: CountryRequest) {
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
                    postProgress(state = request.state, action = request.action, loading =false)
                }
                postResult(state = request.state,
                    action = request.action,
                    data = result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading = false)
                }
                postFailures(state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemsRx(request: CountryRequest): Maybe<List<CountryItem>> {
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: CountryRequest): Maybe<List<Server>> {
        return repo.getItemsRx(request.limit)
    }

    private fun getUiItemsRx(
        request: CountryRequest,
        items: List<Server>
    ): Maybe<List<CountryItem>> {
        return Maybe.create { emitter ->
            val result = Maps.newHashMap<String, CountryItem>()
            for (server in items) {
                val country = mapper.getItem(server.countryCode, server.countryName)
                if (!result.containsKey(server.countryCode)) {
                    val item = getUiItem(request, country!!)
                    item.count = 1
                    result.put(server.countryCode, item)
                } else {
                   val item = result.get(server.countryCode)
                    item?.run {
                        this.count++
                    }
                }
            }
            emitter.onSuccess(result.values.toList())
        }
    }

    private fun getUiItem(request: CountryRequest, item: Country): CountryItem {
        var uiItem: CountryItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = CountryItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        return uiItem
    }

}