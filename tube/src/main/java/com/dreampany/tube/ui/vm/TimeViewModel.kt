package com.dreampany.tube.ui.vm

import android.app.Application
import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.ui.home.model.VideoItem
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 17/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TimeViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: TimeRepo
) : BaseViewModel<Type, Subtype, State, Action, Time, Time, UiTask<Type, Subtype, State, Action, Time>>(
    application,
    rm
) {

    fun loadTime(id: String, type: Type, subtype: Subtype, state: State) {
        uiScope.launch {
            var result: Time? = null
            var errors: SmartError? = null
            try {
                result = repo.read(id, type.value, subtype.value, state.value)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(type, subtype, state, errors)
            } else {
                postResult(type, subtype, state, result)
            }
        }
    }

    private fun postError(type: Type, subtype: Subtype, state: State, error: SmartError) {
        postSingle(
            type,
            subtype,
            state,
            Action.DEFAULT,
            error = error,
            showProgress = false
        )
    }

    private fun postResult(type: Type, subtype: Subtype, state: State, result: Time?) {
        postSingle(
            type,
            subtype,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = false
        )
    }
}