package com.dreampany.network.manager

import android.content.Context
import com.dreampany.framework.misc.AppExecutors
import com.dreampany.framework.misc.RxMapper
import com.dreampany.network.api.Wifi
import com.dreampany.network.data.model.Network
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NetworkManager @Inject constructor(
    private val context: Context,
    private val rx: RxMapper,
    private val ex: AppExecutors,
    private val wifi: Wifi
) {

    interface Callback {
        fun onNetworkResult(networks: List<Network>)
    }

    private var disposable: Disposable? = null
    private val callbacks = mutableSetOf<Callback>()
    private val networks = mutableListOf<Network>()
    private var checkInternet: Boolean = false

    fun observe(callback: Callback, checkInternet: Boolean = false) {
        callbacks.add(callback)
        this.checkInternet = checkInternet
        if (isStarted()) {
            ex.postToUi(Runnable {
                postNetworks(callback)
            })
            return
        }
        disposable = ReactiveNetwork.observeNetworkConnectivity(context)
            .flatMapSingle({ buildNetwork(it) })
            .subscribeOn(rx.io())
            .observeOn(rx.io())
            .subscribe(this::postResult, this::postError)
    }

    fun deObserve(callback: Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            if (isStarted()) {
                disposable?.dispose()
            }
        }
    }

    fun hasInternet(): Boolean {
        for (network in networks) {
            if (network.internet) {
                return true
            }
        }
        return false
    }

    fun isObserving(): Boolean {
        return !callbacks.isEmpty();
    }

    private fun isStarted(): Boolean {
        disposable?.let {
            if (it.isDisposed) {
                return false
            }
            return true
        }
        return false
    }

    private fun postNetworks(callback: Callback) {
        if (!networks.isEmpty()) {
            callback.onNetworkResult(networks)
        }
    }

    private fun postNetworks() {
        for (callback in callbacks) {
            postNetworks(callback)
        }
    }

    private fun postResult(network: Network) {
        Timber.v("postReesult %s", network.toString())
        if (networks.contains(network)) {
            networks.set(networks.indexOf(network), network)
        } else {
            networks.add(network)
        }
        postNetworks()
    }

    private fun postError(error: Throwable) {
        postNetworks()
    }

    private fun buildNetwork(connectivity: Connectivity): Single<Network> {
        Timber.v(
            "Connectivity %s %s %s",
            connectivity.typeName(),
            connectivity.available(),
            connectivity.toString()
        )
        return Single.create({
            val network = wifi.getNetwork()
            if (connectivity.available()) {
                if (checkInternet) {
                    network.internet = ReactiveNetwork.checkInternetConnectivity().blockingGet()
                }
            }
            it.onSuccess(network)
        })
    }
}