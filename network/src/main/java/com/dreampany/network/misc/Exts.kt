package com.dreampany.network.misc

import android.content.Context
import com.dreampany.network.nearby.model.Peer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.common.collect.BiMap

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
const val STRING_EMPTY = ""
val currentMillis: Long get() = System.currentTimeMillis()
val Byte?.value: Byte get() = if (this == null) 0 else this
val ByteArray?.isEmpty: Boolean
    get() {
        if (this == null) return true
        if (this.size == 0) return true
        return false
    }
val ByteArray?.length: Int
    get() = this?.size ?: 0

fun ByteArray.secondOrNull(): Byte? = if (length < 2) null else this[1]

val Boolean?.value: Boolean get() = if (this == null) false else this
val String?.value: String get() = if (this == null) STRING_EMPTY else this
fun Long.isExpired(delay: Long): Boolean = currentMillis - this > delay
fun MutableMap<Long, Boolean>.valueOf(key: Long): Boolean = get(key) ?: false
fun MutableMap<String, Boolean>.valueOf(key: String): Boolean = get(key) ?: false
fun MutableMap<String, Int>.valueOf(key: String): Int = get(key) ?: 0
fun MutableMap<String, Long>.valueOf(key: String): Long = get(key) ?: 0L
fun MutableMap<Long, Long>.valueOf(key: Long): Long = get(key) ?: 0L
fun MutableMap<Long, Long>.timeOf(key: Long): Long = get(key) ?: currentMillis
fun MutableMap<String, Long>.timeOf(key: String): Long = get(key) ?: currentMillis
fun MutableMap<String, Peer.State>.valueOf(key: String): Peer.State = get(key) ?: Peer.State.DEAD
fun BiMap<Long, String>.valueOf(key: Long): String = get(key) ?: STRING_EMPTY
fun BiMap<String, String>.inverseOf(key: String): String? = inverse().get(key)
val Context.hasPlayService: Boolean
    get() = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(applicationContext) == ConnectionResult.SUCCESS

val String.isLong : Boolean
    get() = this.toLongOrNull() != null

val String.isValidPeerId : Boolean
    get() = this.find { !it.isLetterOrDigit() } == null