package com.dreampany.tools.ui.vm.question

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
import com.dreampany.tools.data.mapper.QuestionMapper
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.QuestionRepository
import com.dreampany.tools.ui.misc.QuestionRequest
import com.dreampany.tools.ui.model.question.QuestionItem
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2020-02-21
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutor,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: QuestionMapper,
    private val repo: QuestionRepository,
    private val translationRepo: TranslationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Question, QuestionItem, UiTask<Question>>(application, rx, ex, rm),
    NetworkManager.Callback {

    init {
        network.observe(this, checkInternet = true)
    }

    override fun clear() {
        network.deObserve(this)
        super.clear()
    }

    override fun onNetworks(networks: List<Network>) {

    }

    fun request(request: QuestionRequest) {
        if (request.single) {
            //requestSingle(request)
        } else {
            requestMultiple(request)
        }
    }

    private fun requestMultiple(request: QuestionRequest) {
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
                    data =result)
            }, { error ->
                if (request.progress) {
                    postProgress(state = request.state, action = request.action, loading =false)
                }
                postFailures(state = request.state,
                    action = request.action,
                    error = MultiException(error, ExtraException())
                )
            })
        addMultipleSubscription(disposable)
    }

    private fun requestUiItemsRx(request: QuestionRequest): Maybe<List<QuestionItem>> {
/*        if (request.action == Action.FAVORITE) {
            return storeRepo
                .getItemsRx(Type.SERVER, Subtype.DEFAULT, State.FAVORITE)
                .flatMap { getUiItemsOfStoresRx(request, it) }
        }*/
        return requestItemsRx(request).flatMap { getUiItemsRx(request, it) }
    }

    private fun requestItemsRx(request: QuestionRequest): Maybe<List<Question>> {
        return repo.getItemsRx(request.limit)
    }

    private fun getUiItemsRx(
        request: QuestionRequest,
        items: List<Question>
    ): Maybe<List<QuestionItem>> {
        return Flowable.fromIterable(items)
            .map { getUiItem(request, it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(request: QuestionRequest, item: Question): QuestionItem {
        var uiItem: QuestionItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = QuestionItem.getItem(item)
            mapper.putUiItem(item.id, uiItem)
        }
        uiItem.item = item
        adjustFavorite(item, uiItem)
        return uiItem
    }

    private fun adjustFavorite(item: Question, uiItem: QuestionItem) {
        uiItem.favorite = isFavorite(item)
    }

    private fun isFavorite(item: Question): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(item.id)) {
            val favorite = hasStore(item.id, Type.QUESTION, Subtype.DEFAULT, State.FAVORITE)
            Timber.v("Favorite of %s %s", item.id, favorite)
            favorites.put(item.id, favorite)
        }
        return favorites.get(item.id)
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasStore(id, Type.QUESTION, Subtype.DEFAULT, State.FAVORITE)
        if (favorite) {
            removeStore(id, Type.QUESTION, Subtype.DEFAULT, State.FAVORITE)
            favorites.put(id, false)
        } else {
            putStore(id, Type.QUESTION, Subtype.DEFAULT, State.FAVORITE)
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