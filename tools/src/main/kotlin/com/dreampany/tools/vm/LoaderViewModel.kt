package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.LoadRequest
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Load
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.ui.model.LoadItem
import kotlinx.coroutines.Runnable
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoaderViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Load, LoadItem, UiTask<Load>>(application, rx, ex, rm) {

    private val commonWords = mutableListOf<Word>()
    private val alphaWords = mutableListOf<Word>()

    fun request(request: LoadRequest) {

    }


    fun loadCommons() {
        ex.postToIO(Runnable {

        })
    }

    fun loadAlphas() {

    }

}