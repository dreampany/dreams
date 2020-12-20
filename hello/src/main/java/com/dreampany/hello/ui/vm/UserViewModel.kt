package com.dreampany.hello.ui.vm

import android.app.Application
import com.dreampany.framework.misc.exts.ref
import com.dreampany.framework.misc.func.Mapper
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.hello.data.enums.Action
import com.dreampany.hello.data.enums.State
import com.dreampany.hello.data.enums.Subtype
import com.dreampany.hello.data.enums.Type
import com.dreampany.hello.data.model.User
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.data.source.repo.UserRepo
import com.dreampany.hello.misc.Constants
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val mapper: UserMapper,
    private val repo: UserRepo
) : BaseViewModel<Type, Subtype, State, Action, User, User, UiTask<Type, Subtype, State, Action, User>>(
    application,
    rm
) {

    fun writeDummyUser() {
        uiScope.launch {
            progressSingle(true)
            var result: User? = null
            var errors: SmartError? = null
            try {
                val lastId = repo.lastId()
                var index = -1L
                if (lastId != null)
                    index = mapper.index(lastId)
                index = index.inc()
                val input = User(index.inc().toString())
                input.ref = context.ref
                input.name = "User ${index.inc()}"
                var opt = repo.write(input)
                if (opt > -1) {
                    opt = repo.track(index.inc().toString(), index)
                }
                if (opt > -1) {
                    result = input
                } else {
                    errors = SmartError(error = IllegalStateException())
                }
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


    fun write(input: User) {
        uiScope.launch {
            progressSingle(true)
            var result: User? = null
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

    fun newUsers() {
        uiScope.launch {
            progressSingle(true)
            var result: List<User>? = null
            var errors: SmartError? = null
            try {
                val ids = repo.newIds(Constants.Limit.NEW_USERS)
                if (ids != null && ids.isNotEmpty()) {
                    result = repo.read(ids)
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                //postResult(result)
            }
        }
    }

    private fun progressSingle(progress: Boolean) {
        postProgressSingle(
            Type.USER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postMultiple(
            Type.USER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: User?, state: State = State.DEFAULT) {
        postSingle(
            Type.USER,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}