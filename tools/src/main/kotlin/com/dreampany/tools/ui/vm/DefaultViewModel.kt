package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.DefaultRequest
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.misc.WordRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Runnable;

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class DefaultViewModel
@Inject constructor(
    private val application: Application,
    private val rx: RxMapper,
    private val ex: AppExecutors,
    private val rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val notify: NotifyManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    private val translationRepo: TranslationRepository
) {

    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
    }

    fun request(request: DefaultRequest) {
        when (request.type) {
            Type.QUIZ -> {
                requestQuiz(request)
            }
        }
    }

    private fun requestQuiz(request: DefaultRequest) {
        when (request.subtype) {
            Subtype.RELATED -> {
                ex.postToIO(Runnable {
                    requestRelatedQuiz(request)
                })
            }
        }
    }

    private fun requestRelatedQuiz(request: DefaultRequest) {
        if (pref.has(request.type, request.subtype, request.state)) {
            return
        }

        //val synonymStore = Store()
    }
}