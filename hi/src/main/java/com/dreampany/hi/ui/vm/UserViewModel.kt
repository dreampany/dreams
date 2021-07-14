package com.dreampany.hi.ui.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.data.source.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: Pref,
    private val repo: UserRepo
) : BaseViewModel<Type, Subtype, State, Action, User, User, UiTask<Type, Subtype, State, Action, User>>(
    application, rm
) {

    fun createAnonymousUser() {

    }


}