package com.dreampany.cast.vm

import android.app.Application
import com.dreampany.cast.data.model.User
import com.dreampany.cast.data.source.pref.Pref
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
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper
) : BaseViewModel<User, UserItem, UiTask<User>>(application, rx, ex, rm), NetworkManager.Callback {

    override fun clear() {
        //network.deObserve(this, false)
        super.clear()
    }

    override fun onNetworkResult(network: List<Network>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onResult(vararg networks: Network?) {

    }

    fun startNetwork() {
        network.observe(this, false)
    }

    fun stopNetwork() {

    }
}