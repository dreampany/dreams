package com.dreampany.history.vm

import android.app.Application
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.history.data.misc.HistoryMapper
import com.dreampany.history.data.source.pref.Pref
import com.dreampany.history.data.source.repository.HistoryRepository
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
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
){
}