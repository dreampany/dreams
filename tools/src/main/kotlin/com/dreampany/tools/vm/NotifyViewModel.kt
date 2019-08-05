package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.tools.app.App
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.model.DemoItem
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
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
    private val stateRepo: StateRepository
) {

    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
    }

    fun notifyIf() {

    }

    fun clearIf() {
        disposables.clear()
    }

    private fun postResult(result: List<DemoItem>) {
        val app = application as App
        if (app.isVisible()) {
            //return;
        }
    }

    private fun postFailed(error: Throwable) {
        val app = application as App
        Timber.v(error)
        //app.throwAnalytics(Constants.Event.ERROR, Constants.notifyHistory(application), error)
    }
}