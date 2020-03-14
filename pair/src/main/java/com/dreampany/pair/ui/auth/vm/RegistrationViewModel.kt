package com.dreampany.pair.ui.auth.vm

import android.app.Application
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.pair.data.model.User
import com.dreampany.pair.data.source.repo.RegistrationRepo
import com.dreampany.pair.ui.model.UiTask
import kotlinx.coroutines.launch
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
    private val repo: RegistrationRepo
) : BaseViewModel<User, User, UiTask<User>>(application) {

    fun register(email: String, password: String, name: String) {
        uiScope.launch {
           val user = repo.register(email, password, name)
            //output.value = user
        }
    }


}