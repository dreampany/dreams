package com.dreampany.demo.vm

import android.app.Application
import com.dreampany.demo.data.model.Demo
import com.dreampany.demo.data.source.pref.Pref
import com.dreampany.demo.ui.model.DemoItem
import com.dreampany.demo.ui.model.UiTask
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.vm.BaseViewModel
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
    private val stateMapper: StateMapper,
    private val stateRepo: StateRepository,
    private val translationRepo: TranslationRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Demo, DemoItem, UiTask<Demo>>(application, rx, ex, rm) {
}