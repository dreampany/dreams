package com.dreampany.cast.data.source.repository

import android.content.Context
import com.dreampany.cast.data.model.User
import com.dreampany.frame.data.source.repository.RepositoryKt
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.nearby.NearbyApi
import com.dreampany.nearby.model.Peer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/16/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NearbyRepository @Inject constructor(
    private val context: Context,
    rx: RxMapper,
    rm: ResponseMapper,
    private val nearby: NearbyApi
) : RepositoryKt<String, User>(rx, rm), NearbyApi.Callback {

    interface UserCallback {
        fun onJoin(user: User)

        fun onUpdate(user: User)

        fun onLeave(user: User)
    }

    private var serviceId: Long = 0
    private val owner: User
    private val ownerId: Long

    private val userCallbacks: MutableSet<UserCallback>

    init {
        serviceId = DataUtil.getSha512(AndroidUtil.getPackageName(context))
        owner = buildOwner()
        ownerId = DataUtil.getSha512(owner.id)

        nearby.register(this)
        userCallbacks = mutableSetOf()
    }

    override fun onPeer(peer: Peer, state: Peer.State) {

    }

    override fun onData(peer: Peer, data: ByteArray) {
    }

    override fun onStatus(
        payloadId: Long,
        state: NearbyApi.PayloadState,
        totalBytes: Long,
        bytesTransferred: Long
    ) {
    }

    fun register(callback: UserCallback) {
        userCallbacks.add(callback)
        start()
    }

    fun unregister(callback: UserCallback) {
        userCallbacks.remove(callback)
        stop()
    }

    private fun start() {
        if (!userCallbacks.isEmpty() /*|| !discoverCallbacks.isEmpty()*/) {
            nearby.init(context, serviceId, ownerId)
            nearby.start()
        }
        /*        if (clients <= 0) {
            nearby.start();
        }
        clients++;*/
    }

    private fun stop() {
        if (userCallbacks.isEmpty() /*&& discoverCallbacks.isEmpty()*/) {
            nearby.stop()
        }
        /*        clients--;
        if (clients <= 0) {
            nearby.stop();
        }*/
    }

    private fun buildOwner(): User {
        val id = AndroidUtil.getAndroidId(context)
        val time = TimeUtil.currentTime()
        val name = AndroidUtil.getDeviceName()
        val user = User(id, time)
        user.name = name
        return user
    }
}