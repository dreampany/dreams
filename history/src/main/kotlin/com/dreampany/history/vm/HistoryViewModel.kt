package com.dreampany.history.vm

import android.app.Application
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.history.data.enums.HistoryType
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
) : BaseViewModel<History, HistoryItem, UiTask<History>>(application, rx, ex, rm), HistoryItem.OnLinkClickListener {

    interface OnClickListener {
        fun onLinkClicked(link: String)
    }

    private var clickListener: OnClickListener? = null

    override fun onLickClicked(link: String) {
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

    private fun loadUiItemsRx(request: HistoryRequest): Maybe<List<HistoryItem>> {
        return repo.getItemsRx(request.type, request.day, request.month)
            .flatMap { getUiItemsRx(it) }
    }

    private fun getUiItemsRx(inputs: List<History>): Maybe<List<HistoryItem>> {
        return Flowable.fromIterable(inputs)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(input: History): HistoryItem {
        val map = uiMap
        var uiItem: HistoryItem? = map.get(input.id)
        if (uiItem == null) {
            uiItem = HistoryItem.getItem(input, this)
            map.put(input.id, uiItem)
        }
        uiItem.item = input
        uiItem.linkClickListener = this
        return uiItem
    }
}