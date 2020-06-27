package com.dreampany.nearby.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.exts.deviceId
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.nearby.BuildConfig
import com.dreampany.nearby.app.App
import com.dreampany.nearby.data.enums.Action
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.nearby.data.source.repo.UserRepo
import com.dreampany.nearby.ui.home.model.UserItem
import com.google.android.gms.nearby.connection.Strategy
import com.jaredrummler.android.device.DeviceName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by roman on 21/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val repo: UserRepo
) : BaseViewModel<Type, Subtype, State, Action, User, UserItem, UiTask<Type, Subtype, State, Action, User>>(
    application,
    rm
), UserDataSource.Callback {

    override fun onCleared() {
        repo.stopNearby()
        super.onCleared()
    }

    override fun onUser(user: User, live: Boolean) {
        uiScope.launch {
            postResult(user.toItem(live, false))
        }
    }

    fun startNearby() {
        uiScope.launch {
            startNearbyImp()
        }
    }

    private suspend fun startNearbyImp() = withContext(Dispatchers.IO) {
        val type = pref.getNearbyType()
        val serviceId = BuildConfig.APPLICATION_ID
        val deviceId = getApplication<App>().deviceId
        val deviceName = DeviceName.getDeviceName()
        val user = User(deviceId)
        user.name = deviceName
        repo.register(this@UserViewModel)
        repo.startNearby(type, serviceId, user)
    }

    private fun User.toItem(live: Boolean, favorite: Boolean): UserItem {
        return UserItem(this, live = live, favorite = favorite)
    }

    private fun postResult(result: UserItem?, state: State = State.DEFAULT) {
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