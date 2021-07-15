package com.dreampany.hi.ui.vm

import android.app.Application
import com.dreampany.common.misc.exts.applicationId
import com.dreampany.common.misc.exts.deviceId
import com.dreampany.common.misc.exts.hash256
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.hi.app.App
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.data.source.repo.UserRepo
import com.dreampany.network.nearby.core.NearbyApi
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
        val user = User(hash256)
        user.name = getApplication<App>().deviceId
        pref.user = user
    }

    fun nearbyUsers() {
        val serviceId = getApplication<App>().applicationId
        val user = pref.user ?: return
        repo.startNearby(NearbyApi.Type.PTP, serviceId, user)
    }

}