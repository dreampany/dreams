package com.dreampany.tools.ui.fragment

import android.os.Bundle
import com.dreampany.framework.api.service.JobManager
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.service.NotifyService
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
    internal lateinit var job: JobManager
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
        ex.postToUi (Runnable{ this.initView() })
    }

    override fun onStopUi() {
        disposables.clear()
    }

    private fun initView() {
        setTitle(R.string.settings)
        val notifyEvent = getString(R.string.key_notification)
        val notifyFlowable = pref.observePublicly(notifyEvent, Boolean::class.java, true)

        disposables.add(rx
            .backToMain(notifyFlowable)
            .subscribe { enabled -> adjustNotify() })
    }

    private val runner = Runnable { this.configJob() }

    private fun configJob() {
        if (pref.hasNotification()) {
            job.create(
                Constants.Tag.NOTIFY_SERVICE,
                NotifyService::class,
                Constants.Time.NOTIFY.toInt(),
                Constants.Period.NOTIFY.toInt()
            )
        } else {
            job.cancel(Constants.Tag.NOTIFY_SERVICE)
        }
    }

    private fun adjustNotify() {
        ex.postToUi(runner, 2000)
    }
}