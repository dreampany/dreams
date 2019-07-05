package com.dreampany.word.vm

import android.app.Application
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.ApiRepository
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordViewModelKt @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper,
    val repo: ApiRepository
) : BaseViewModel<Word, WordItem, UiTask<Word>>(application, rx, ex, rm) {

    fun load(request: WordRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
    }

}