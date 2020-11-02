package com.dreampany.radio.ui.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.data.source.repo.StationRepo
import com.dreampany.radio.ui.model.StationItem
import javax.inject.Inject

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StationViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: StationRepo
) : BaseViewModel<Type, Subtype, State, Action, Station, StationItem, UiTask<Type, Subtype, State, Action, Station>>(
    application,
    rm
) {
}