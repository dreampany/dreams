package com.dreampany.history.vm

import android.app.Application
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.enums.ItemState
import com.dreampany.history.data.enums.ItemSubtype
import com.dreampany.history.data.enums.ItemType
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.data.source.pref.Pref
import com.dreampany.history.data.source.repository.HistoryRepository
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.UiTask
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper,
    val stateRepo: StateRepository,
    val mapper: HistoryMapper,
    val repo: HistoryRepository,
    val translationRepo: TranslationRepository,
    @Favorite val favorites: SmartMap<String, Boolean>
) : BaseViewModel<History, HistoryItem, UiTask<History>>(application, rx, ex, rm),
    HistoryItem.OnClickListener {

    interface OnClickListener : HistoryItem.OnClickListener {

    }

    private var clickListener: OnClickListener? = null

    override fun onFavoriteClicked(history: History) {
        clickListener?.onFavoriteClicked(history)
    }

    override fun onLinkClicked(link: String) {
        clickListener?.onLinkClicked(link)
    }

    fun setOnLinkClickListener(clickListener: OnClickListener?) {
        this.clickListener = clickListener
    }

    fun setHistoryType(type: HistoryType) {
        pref.setHistoryType(type)
    }

    fun setDay(day: Int) {
        pref.setDay(day)
    }

    fun setMonth(month: Int) {
        pref.setMonth(month)
    }

    fun getHistoryType(): HistoryType {
        return pref.getHistoryType()
    }

    fun getDay(): Int {
        return pref.getDay()
    }

    fun getMonth(): Int {
        return pref.getMonth()
    }

    fun getYear(): Int {
        return pref.getYear()
    }

    fun load(request: HistoryRequest) {
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
                postResult(Response.Type.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addMultipleSubscription(disposable)
    }

    fun toggleFavorite(history: History) {
        val disposable = rx
            .backToMain(toggleImpl(history))
            .subscribe({ result ->
                postResult(Response.Type.UPDATE, result)
            }, { this.postFailure(it) })
    }

    private fun toggleImpl(history: History): Maybe<HistoryItem> {
        return Maybe.fromCallable {
            toggleFavorite(history.id)
            getUiItem(history)
        }
    }

    private fun toggleFavorite(id: String): Boolean {
        val favorite = hasState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
        if (favorite) {
            removeState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            favorites.put(id, false)
        } else {
            putState(id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            favorites.put(id, true)
        }
        return favorites.get(id)
    }

    private fun removeState(id: String, subtype: ItemSubtype, state: ItemState): Int {
        val s = State(id, ItemType.HISTORY.name, subtype.name, state.name)
        return stateRepo.delete(s)
    }

    private fun putState(id: String, subtype: ItemSubtype, state: ItemState): Long {
        val s = State(id, ItemType.HISTORY.name, subtype.name, state.name)
        s.time = TimeUtil.currentTime()
        return stateRepo.putItem(s)
    }

    private fun hasState(id: String, subtype: ItemSubtype, state: ItemState): Boolean {
        return stateRepo.getCountById(id, ItemType.HISTORY.name, subtype.name, state.name) > 0
    }

    private fun loadUiItemsRx(request: HistoryRequest): Maybe<List<HistoryItem>> {
        return if (request.favorite) {
            stateRepo.getItemsRx(
                ItemType.HISTORY.name,
                ItemSubtype.DEFAULT.name,
                ItemState.FAVORITE.name
            ).flatMap { getFavoriteUiItemsRx(it) }
        } else {
            repo.getItemsRx(
                request.type,
                request.day,
                request.month
            ).flatMap { getUiItemsRx(it) }
        }
    }

    private fun getUiItemsRx(inputs: List<History>): Maybe<List<HistoryItem>> {
        return Flowable.fromIterable(inputs)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }

    private fun getFavoriteUiItemsRx(inputs: List<State>): Maybe<List<HistoryItem>> {
        return Flowable.fromIterable(inputs)
            .map { getFavoriteUiItem(it) }
            .toList()
            .toMaybe()
    }

    private fun getFavoriteUiItem(input: State): HistoryItem {
        val item = mapper.toItem(input, repo)
        return getUiItem(item)
    }

    private fun getUiItem(input: History): HistoryItem {
        val map = uiMap
        var uiItem: HistoryItem? = map.get(input.id)
        if (uiItem == null) {
            uiItem = HistoryItem.getItem(input, this)
            map.put(input.id, uiItem)
        }
        uiItem.item = input
        uiItem.clickListener = this
        uiItem.favorite = isFavorite(input)
        return uiItem
    }

    private fun isFavorite(history: History): Boolean {
        Timber.v("Checking favorite")
        if (!favorites.contains(history.id)) {
            val favorite = hasState(history.id, ItemSubtype.DEFAULT, ItemState.FAVORITE)
            Timber.v("Favorite of %s %s", history.id, favorite)
            favorites.put(history.id, favorite)
        }
        return favorites.get(history.id)
    }
}