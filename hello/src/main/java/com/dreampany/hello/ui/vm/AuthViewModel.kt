package com.dreampany.hello.ui.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.source.repo.AuthRepo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: AuthRepo
) : BaseViewModel<Type, Subtype, State, Action, Auth, Auth, UiTask<Type, Subtype, State, Action, Auth>>(
    application,
    rm
) {
    fun write(input: Auth) {
        uiScope.launch {
            progressSingle(true)
            var result: Auth? = null
            var errors: SmartError? = null
            try {
                val opt = repo.write(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }

    fun read(id: String) {
        uiScope.launch {
            progressSingle(true)
            var result: Auth? = null
            var errors: SmartError? = null
            try {
                result = repo.read(id)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }

    fun readByEmail(email: String) {
        uiScope.launch {
            progressSingle(true)
            var result: Auth? = null
            var errors: SmartError? = null
            try {
                result = repo.readByEmail(email)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result)
            }
        }
    }


    private fun progressSingle(progress: Boolean) {
        postProgressSingle(
            Type.AUTH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postMultiple(
            Type.AUTH,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: Auth?, state: State = State.DEFAULT) {
        postSingle(
            Type.AUTH,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}