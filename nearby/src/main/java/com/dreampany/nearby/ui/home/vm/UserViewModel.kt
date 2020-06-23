package com.dreampany.nearby.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.exts.deviceId
import com.dreampany.framework.misc.exts.hash256
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.nearby.BuildConfig
import com.dreampany.nearby.app.App
import com.dreampany.nearby.data.enums.*
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.data.source.api.UserDataSource
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.nearby.data.source.repo.UserRepo
import com.dreampany.nearby.ui.home.model.UserItem
import com.google.android.gms.nearby.connection.Strategy
import javax.inject.Inject
import kotlin.math.absoluteValue

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

    override fun onUser(user: User, live: Boolean) {

    }

    fun startNearby() {
        val strategy = Strategy.P2P_STAR
        val serviceId = BuildConfig.APPLICATION_ID.hash256
        val deviceId = getApplication<App>().deviceId.hash256
        val userId = deviceId.toString()
        val user = User(userId)
        repo.register(this)
        repo.startNearby(strategy, serviceId, user)
    }

}