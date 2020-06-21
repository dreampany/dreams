package com.dreampany.network.nearby.core

import com.dreampany.network.misc.isEmpty
import com.google.common.hash.Hashing
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by roman on 19/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Packets {

    companion object {
        private const val TYPE_PEER: Byte = 1
        private const val TYPE_DATA: Byte = 2
        private const val TYPE_FILE: Byte = 3

        private const val SUBTYPE_ID: Byte = 1
        private const val SUBTYPE_META: Byte = 2
        private const val SUBTYPE_OKAY: Byte = 3
        private const val SUBTYPE_DATA: Byte = 4
        private const val SUBTYPE_FILE_PAYLOAD_ID: Byte = 5

        private const val SUBTYPE_META_REQUEST: Byte = 6
        private const val SUBTYPE_DATA_REQUEST: Byte = 7

        val ByteArray?.isPeer: Boolean
            get() {
                if (this.isEmpty) return false
                return this?.get(0) == TYPE_PEER
            }

        val ByteArray?.isData: Boolean
            get() {
                if (this.isEmpty) return false
                return this?.get(0) == TYPE_DATA
            }

        val ByteArray?.isFile: Boolean
            get() {
                if (this.isEmpty) return false
                return this?.get(0) == TYPE_FILE
            }

        val hash256 : Long
            get() = UUID.randomUUID().toString().hash256

        val ByteArray?.hash256: Long
            get() {
                if (this == null || isEmpty) return 0L
                return Hashing.sha256().newHasher().putBytes(this).hash().asLong()
            }

        val String?.hash256: Long
            get() = this?.toByteArray().hash256

        val Long.peerMetaPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + 8)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_META)
                buf.putLong(this)
                return buf.array()
            }
        val peerOkayPacket: ByteArray = ByteBuffer.allocate(1 + 1)
            .put(TYPE_PEER)
            .put(SUBTYPE_OKAY)
            .array()
        val ByteArray.peerDataPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + size)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_DATA)
                buf.put(this)
                return buf.array()
            }
        val ByteArray.dataPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + size)
                buf.put(TYPE_DATA)
                buf.put(this)
                return buf.array()
            }
    }
}