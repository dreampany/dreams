package com.dreampany.word.ui.fragment

import android.os.Bundle
import com.dreampany.frame.api.service.ServiceManager
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.word.R
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.misc.Constants
import com.dreampany.word.service.NotifyService
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/29/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SettingsFragment @Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var rx: RxMapper
    @Inject
    internal lateinit var service: ServiceManager
    @Inject
    internal lateinit var pref: Pref
    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
    }

    override fun getPrefLayoutId(): Int {
        return R.xml.settings
    }

    override fun getScreen(): String {
        return Constants.settings(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        ex.postToUi { this.initView() }
    }

    override fun onStopUi() {
        disposables.clear()
    }

    private fun initView() {
        setTitle(R.string.settings)
        val notifyWordSync = getString(R.string.key_word_sync)
        val eventFlowable = pref.observePublicly(notifyWordSync, Boolean::class.java, true)

        disposables.add(rx
            .backToMain(eventFlowable)
            .subscribe { enabled -> adjustNotify() })
    }

    private val runner = Runnable { this.configJob() }

    private fun configJob() {
        if (pref.hasNotification()) {
            service.schedulePowerService(NotifyService::class.java, Constants.Time.NotifyPeriod.toInt())
        } else {
            service.cancel(NotifyService::class.java)
        }
    }

    private fun adjustNotify() {
        ex.postToUi(runner, 2000)
    }
}