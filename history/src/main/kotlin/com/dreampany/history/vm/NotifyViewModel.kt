package com.dreampany.history.vm

import android.app.Application
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.util.NumberUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.history.R
import com.dreampany.history.app.App
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.data.source.pref.Pref
import com.dreampany.history.data.source.repository.HistoryRepository
import com.dreampany.history.misc.Constants
import com.dreampany.history.ui.activity.NavigationActivity
import com.dreampany.history.ui.enums.UiSubtype
import com.dreampany.history.ui.enums.UiType
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.UiTask
import com.dreampany.network.manager.NetworkManager
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NotifyViewModel
@Inject constructor(
    private val application: Application,
    private val rx: RxMapper,
    private val ex: AppExecutors,
    private val rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val notify: NotifyManager,
    private val stateMapper: StateMapper,
    private val stateRepo: StateRepository,
    private val mapper: HistoryMapper,
    private val repo: HistoryRepository
) {

    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
    }

    fun notifyIf() {
        val day = pref.getDay()
        val month = pref.getMonth()

        val requestEvent = HistoryRequest(HistoryType.EVENT, day, month, true, false, false, true)
        val requestBirth = HistoryRequest(HistoryType.BIRTH, day, month, true, false, false, true)
        val requestDeath = HistoryRequest(HistoryType.DEATH, day, month, true, false, false, true)
        load(requestEvent)
        load(requestBirth)
        load(requestDeath)
    }

    fun clearIf() {
        disposables.clear()
    }

    private fun load(request: HistoryRequest) {
        if (!TimeUtil.isExpired(pref.getNotifyHistoryTime(request.type), Constants.Time.NotifyNextHistory)) {
            return
        }
        pref.commitNotifyHistoryTime(request.type)
        Timber.v("Fire Notification")
        disposables.add(rx
            .backToMain(loadUiItemsRx(request))
            .subscribe({ this.postResult(it) }, { this.postFailed(it) })
        )
    }

    private fun loadUiItemsRx(request: HistoryRequest): Maybe<List<HistoryItem>> {
        return repo.getItemsRx(
            request.type,
            request.day,
            request.month
        ).flatMap {
            getUiItemsRx(it)
        }
    }

    private fun getUiItemsRx(inputs: List<History>): Maybe<List<HistoryItem>> {
        return Flowable.fromIterable(inputs)
            .map { getUiItem(it) }
            .toList()
            .toMaybe()
    }

    private fun getUiItem(input: History): HistoryItem {
        val uiItem = HistoryItem.getItem(input)
        return uiItem
    }

    private fun postResult(result: List<HistoryItem>) {
        val app = application as App
        if (app.isVisible()) {
            //return;
        }

        val uiItem = result.get(NumberUtil.nextRand(result.size))
        val item = uiItem.item

        val title = TextUtil.getString(app, R.string.notify_title_history, item.type!!.toTitle())
        val message: String? = item.text
        val targetClass: Class<*> = NavigationActivity::class.java

        val task = UiTask(false, UiType.HISTORY, UiSubtype.VIEW, item)
        notify.showNotification(title!!, message!!, R.drawable.ic_notification, targetClass, task)
        app.throwAnalytics(
            Constants.Event.NOTIFICATION,
            Constants.notifyHistory(application, item.type!!)
        )
    }

    private fun postFailed(error: Throwable) {
        val app = application as App
        Timber.v(error)
        app.throwAnalytics(Constants.Event.ERROR, Constants.notifyHistory(application), error)
    }
}