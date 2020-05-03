package com.dreampany.network.manager

import android.content.Context
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.misc.func.RxMapper
import com.dreampany.network.api.Wifi
import com.dreampany.network.data.model.Network
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Created by Roman-372 on 7/1/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NetworkManager
@Inject constructor(
    private val context: Context,
    private val rx: RxMapper,
    private val ex: Executors,
    private val wifi: Wifi
) {

    interface Callback {
        fun onNetworks(networks: List<Network>)
    }

    private var disposable: Disposable? = null
    private val callbacks: MutableSet<Callback>
    private val networks: MutableList<Network>
    private var checkInternet = false
    private var internet = false
        get

    init {
        callbacks = Collections.synchronizedSet(HashSet())
        networks = Collections.synchronizedList(ArrayList())
    }

    fun observe(callback: Callback, checkInternet: Boolean = false) {
        callbacks.add(callback)
        this.checkInternet = checkInternet
        if (isStarted()) {
            ex.postToUi(Runnable { postNetworks(callback) })
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
            callback.onNetworks(networks)
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
        return Single.create { emitter ->
            val network = getNetwork()
            network.connected = connectivity.available()
            if (connectivity.available()) {
                if (checkInternet) {
                    val settings = InternetObservingSettings.builder()
                        .host("www.google.com")
                        .strategy(SocketInternetObservingStrategy())
                        .build()
                    internet = ReactiveNetwork.checkInternetConnectivity(settings).blockingGet()
                    network.internet = internet
                }
            }
            if (emitter.isDisposed) return@create
            emitter.onSuccess(network)
        }
    }

    private fun getNetwork(): Network {
        return Network(Network.Type.DEFAULT)
    }
}