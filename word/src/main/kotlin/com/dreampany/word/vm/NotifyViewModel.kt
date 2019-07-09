package com.dreampany.word.vm

import android.app.Application
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.WordRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class NotifyViewModel @Inject constructor(
    val application: Application,
    val rx: RxMapper,
    val ex: AppExecutors,
    val rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper,
    val worRepo: WordRepository,
    val translationRepo: TranslationRepository
) {
}