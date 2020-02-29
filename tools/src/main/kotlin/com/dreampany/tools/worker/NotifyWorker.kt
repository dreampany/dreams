package com.dreampany.tools.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.tools.ui.vm.NotifyViewModel
import com.dreampany.framework.api.worker.BaseWorker
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.worker.factory.IWorkerFactory
import com.dreampany.language.Language
import com.dreampany.tools.ui.request.WordRequest
import com.dreampany.tools.data.source.pref.Pref
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by roman on 2019-04-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyWorker(
    context: Context,
    params: WorkerParameters,
    private val pref: Pref,
    private val vm: NotifyViewModel
) : BaseWorker(context, params) {

    override fun onStart(): Result {
        Timber.v("NotifyWorker Started")
        val language = pref.getLanguage(Language.ENGLISH)
        val translate = !Language.ENGLISH.equals(language)
        val syncRequest = WordRequest(
            sourceLang = Language.ENGLISH.code,
            targetLang = language.code,
            history = true,
            translate = translate,
            type = Type.WORD,
            action = Action.SYNC,
            single = true
        )
        vm.request(syncRequest)
        return Result.retry()
    }

    override fun onStop() {
        Timber.v("NotifyWorker Stopped")
        vm.clear()
    }

    class Factory
    @Inject constructor(
        private val pref: Pref,
        private val vm: Provider<NotifyViewModel>
    ) : IWorkerFactory<NotifyWorker> {
        override fun create(context: Context, params: WorkerParameters): NotifyWorker {
            return NotifyWorker(context, params, pref, vm.get())
        }

    }
}