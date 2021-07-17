package com.dreampany.hi.ui.vm

import android.app.Application
import com.dreampany.common.misc.exts.applicationId
import com.dreampany.common.misc.exts.deviceId
import com.dreampany.common.misc.exts.hash256
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.device.DeviceInfo
import com.dreampany.hi.app.App
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.UserDataSource
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.data.source.repo.UserRepo
import com.dreampany.network.nearby.core.NearbyApi
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class UserViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val deviceInfo: DeviceInfo,
    private val pref: Pref,
    private val repo: UserRepo
) : BaseViewModel<Type, Subtype, State, Action, User, User, UiTask<Type, Subtype, State, Action, User>>(
    application, rm
), UserDataSource.Callback {

    override fun onUser(user: User, live: Boolean) {
        Timber.v("User[%s]-live[%s]", user.toString(), live)
    }

    fun registerNearby() {
        repo.register(this)
    }

    fun unregisterNearby() {
        repo.unregister(this)
    }


    fun createAnonymousUser() {
        val deviceId = getApplication<App>().deviceId
        val user = User(deviceId)
        user.name = deviceInfo.model
        pref.user = user
    }

    fun nearbyUsers() {
        val serviceId = getApplication<App>().applicationId
        val user = pref.user ?: return
        repo.startNearby(NearbyApi.Type.PTP, serviceId, user)
    }



}