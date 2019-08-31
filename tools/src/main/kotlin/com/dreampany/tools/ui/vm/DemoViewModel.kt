package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.tools.data.model.Demo
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.model.DemoItem
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.ui.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import javax.inject.Inject

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DemoViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val translationRepo: TranslationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Demo, DemoItem, UiTask<Demo>>(application, rx, ex, rm) {
}