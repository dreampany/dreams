package com.dreampany.pair.ui.auth.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.pair.data.enums.Subtype
import com.dreampany.pair.data.enums.Type
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.repo.RegistrationRepo
import com.dreampany.pair.ui.model.UiTask
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: RegistrationRepo
) : BaseViewModel<User, User, UiTask<User, Type, Subtype>, Type, Subtype>(application, rm) {

    fun register(email: String, password: String, name: String) {
        uiScope.launch {

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

                return@launch
            }
            if (result != null) {
                return@launch
            }
        }
    }


}