package com.dreampany.tools.ui.vm.resume

import android.app.Application
import com.dreampany.framework.data.enums.Action
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
import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.ResumeRepository
import com.dreampany.tools.data.source.repository.ServerRepository
import com.dreampany.tools.ui.misc.ResumeRequest
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.ui.model.ResumeItem
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.google.common.collect.Maps
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResumeViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ResumeMapper,
    private val repo: ResumeRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Resume, ResumeItem, UiTask<Resume>>(application, rx, ex, rm),
    NetworkManager.Callback {

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: ResumeRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: ResumeRequest) {
        if (!takeAction(request.important, multipleDisposable)) {
            return
        }

        val disposable = rx
            .backToMain(requestUiItemsRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(request.state, request.action, true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postResult(request.state, request.action, result)
            }, { error ->
                if (request.progress) {
                    postProgress(request.state, request.action, false)
                }
                postFailures(request.state, request.action, MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemsRx(request: ResumeRequest): Maybe<List<ResumeItem>> {
/*        if (request.action == Action.FAVORITE) {
            return storeRepo
                .getItemsRx(Type.SERVER, Subtype.DEFAULT, State.FAVORITE)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }*/
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: ResumeRequest): Maybe<List<Resume>> {
        return repo.getItemsRx(request.limit)
    }

    private fun getUiItemsRx(
        request: ResumeRequest,
        items: List<Resume>
    ): Maybe<List<ResumeItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: ResumeRequest, item: Resume): ResumeItem {
        var uiItem: ResumeItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = ResumeItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        //adjustFavorite(item, uiItem)
        //adjustTranslate(request, uiItem)
        return uiItem
    }
}