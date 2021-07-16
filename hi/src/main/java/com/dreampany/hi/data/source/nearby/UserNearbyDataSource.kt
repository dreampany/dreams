package com.dreampany.hi.data.source.nearby

import android.content.Context
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.UserDataSource
import com.dreampany.hi.data.source.mapper.UserMapper
import com.dreampany.network.nearby.NearbyManager
import com.dreampany.network.nearby.core.NearbyApi
import com.dreampany.network.nearby.model.Peer
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class UserNearbyDataSource(
    private val context: Context,
    private val mapper: UserMapper,
    private val nearby: NearbyManager
) : UserDataSource, NearbyApi.Callback {

    private val callbacks: MutableSet<UserDataSource.Callback>

    init {
        callbacks = Collections.synchronizedSet(HashSet<UserDataSource.Callback>())
    }

    @Throws
    override fun register(callback: UserDataSource.Callback) {
        callbacks.add(callback)
    }

    @Throws
    override fun unregister(callback: UserDataSource.Callback) {
        callbacks.remove(callback)
    }

    @Throws
    override fun startNearby(type: NearbyApi.Type, serviceId: String, user: User) {
        nearby.register(this)
        nearby.init(type, serviceId, user.id, mapper.readUserToByteArray(user))
        nearby.start()
    }

    @Throws
    override fun stopNearby() {
        nearby.unregister(this)
        nearby.stop()
    }

    override suspend fun isFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readFavorites(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: User): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(inputs: List<User>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun read(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(ids: List<String>): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun reads(offset: Long, limit: Long): List<User>? {
        TODO("Not yet implemented")
    }

    override fun onPeer(peer: Peer, state: Peer.State) {
        Timber.v("Peer [%s]", peer.id)
        val user = mapper.read(peer)
        Timber.v("User [%s]", user.name)
        callbacks.forEach {
            it.onUser(user, state == Peer.State.LIVE)
        }
    }

    override fun onData(peer: Peer, data: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun onStatus(
        payloadId: Long,
        state: NearbyApi.PayloadState,
        totalBytes: Long,
        bytesTransferred: Long
    ) {
        TODO("Not yet implemented")
    }
}