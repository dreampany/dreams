package com.dreampany.network.manager

import android.content.Context
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.RxMapper
import com.dreampany.network.api.Wifi
import com.dreampany.network.data.model.Network
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NetworkManager constructor(
    val context: Context,
    val rx: RxMapper,
    val ex: AppExecutors,
    val wifi: Wifi
) {

    interface Callback {
        fun onNetworkResult(network: List<Network>)
    }

    private var disposable: Disposable? = null
    private val callbacks = mutableSetOf<Callback>()
    private val networks = mutableListOf<Network>()
    private var checkInternet: Boolean = false

    fun observe(callback: Callback, checkInternet: Boolean) {
        this.checkInternet = checkInternet
        if (isStarted()) {
            ex.postToUi({
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

    }

    private fun postError(error: Throwable) {

    }

    private fun buildNetwork(connectivity: Connectivity): Single<Network> {
        Timber.v(
            "Connectivity %s %s %s",
            connectivity.typeName(),
            connectivity.available(),
            connectivity.toString()
        )
        return Single.create({
            if (connectivity.available()) {
                val network = wifi.getNetwork()
                if (checkInternet) {
                    network.internet = ReactiveNetwork.checkInternetConnectivity().blockingGet()
                }
                it.onSuccess(network)
            }
        })
    }
}