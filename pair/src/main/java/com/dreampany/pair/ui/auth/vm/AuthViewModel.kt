package com.dreampany.pair.ui.auth.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.pair.data.enums.Subtype
import com.dreampany.pair.data.enums.Type
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.repo.AuthRepo
import com.dreampany.pair.ui.model.UiTask
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: AuthRepo
) : BaseViewModel<User, User, UiTask<User, Type, Subtype>, Type, Subtype>(application, rm) {

    fun setJoinPressed(status: Boolean) {
        repo.setJoinPressed(status)
    }

    fun isJoinPressed(): Boolean {
        return repo.isJoinPressed()
    }

    fun setLoggedIn(status: Boolean) {
        repo.setLoggedIn(status)
    }

    fun isLoggedIn(): Boolean {
        return repo.isLoggedIn()
    }

    fun setLoggedOut(status: Boolean) {
        repo.setLoggedOut(status)
    }

    fun isLoggedOut(): Boolean {
        return repo.isLoggedOut()
    }

    fun register(email: String, password: String, name: String) {
        uiScope.launch {
            postProgressSingle(Type.USER, Subtype.DEFAULT, progress = true)
            var result: User? = null
            var errors: Throwable? = null
            try {
                result = repo.register(email, password, name)
                Timber.v("Registered %s", result?.id)
            } catch (error: Throwable) {
                Timber.e(error)
                errors = error
            }

            if (errors != null) {
                postSingle(Type.USER, Subtype.DEFAULT, error = errors, showProgress = true)
            } else if (result != null) {
                postSingle(Type.USER, Subtype.DEFAULT, result = result, showProgress = true)
            }
        }
    }

    fun login(email: String, password: String) {
        uiScope.launch {
            postProgressSingle(Type.USER, Subtype.DEFAULT, progress = true)
            var result: User? = null
            var errors: Throwable? = null
            try {
                result = repo.login(email, password)
                Timber.v("Registered %s", result?.id)
            } catch (error: Throwable) {
                Timber.e(error)
                errors = error
            }

            if (errors != null) {
                postSingle(Type.USER, Subtype.DEFAULT, error = errors, showProgress = true)
            } else if (result != null) {
                postSingle(Type.USER, Subtype.DEFAULT, result = result, showProgress = true)
            }
        }
    }

}