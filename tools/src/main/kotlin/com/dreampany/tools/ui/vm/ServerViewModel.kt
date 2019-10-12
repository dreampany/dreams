package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.*
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ServerRepository
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ServerViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ServerMapper,
    private val repo: ServerRepository,
    private val translationRepo: TranslationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Server, ServerItem, UiTask<Server>>(application, rx, ex, rm),
    NetworkManager.Callback {

    private lateinit var uiCallback: SmartAdapter.Callback<ServerItem>

    init {
        network.observe(this, checkInternet = true)
    }

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun setUiCallback(callback: SmartAdapter.Callback<ServerItem>) {
        this.uiCallback = callback
    }

    fun request(request: ServerRequest) {
        if (request.single) {
            requestSingle(request)
        }
    }

    private fun requestSingle(request: ServerRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(request.state, request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun requestUiItemRx(request: ServerRequest): Maybe<ServerItem> {
        return requestItemRx(request).flatMap { getUiItemRx(request, it) }
    }

    private fun requestItemRx(request: ServerRequest): Maybe<Server> {
        return repo.getRandomItemRx()
    }

    private fun getUiItemRx(request: ServerRequest, item: Server): Maybe<ServerItem> {
        Timber.v("%s", item.toString())
        return Maybe.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val uiItem = getUiItem(request, item)
            emitter.onSuccess(uiItem)
        }
    }

    private fun getUiItem(request: ServerRequest, item: Server): ServerItem {
        var uiItem: ServerItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = ServerItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }
}