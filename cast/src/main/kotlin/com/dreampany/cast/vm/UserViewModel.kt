package com.dreampany.cast.vm

import android.app.Application
import com.dreampany.cast.data.model.User
import com.dreampany.cast.data.source.pref.Pref
import com.dreampany.cast.data.source.repository.NearbyRepository
import com.dreampany.cast.ui.model.UiTask
import com.dreampany.cast.ui.model.UserItem
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 6/27/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val stateMapper: StateMapper,
    private val nearby: NearbyRepository
) : BaseViewModel<User, UserItem, UiTask<User>>(application, rx, ex, rm),
    NetworkManager.Callback,
    NearbyRepository.UserCallback {

    override fun clear() {
        //network.deObserve(this, false)
        super.clear()
    }

    override fun onNetworkResult(network: List<Network>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onJoin(user: User) {
    }

    override fun onUpdate(user: User) {
    }

    override fun onLeave(user: User) {
    }

    fun onResult(vararg networks: Network?) {

    }

    fun startNetwork() {
        network.observe(this, false)
    }

    fun stopNetwork() {

    }

    fun startNearby() {
        nearby.register(this)
    }

    fun stopNearby() {
        nearby.unregister(this)
    }
}