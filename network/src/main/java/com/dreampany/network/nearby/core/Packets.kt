package com.dreampany.network.nearby.core

import com.dreampany.network.misc.STRING_EMPTY
import com.dreampany.network.misc.isEmpty
import com.dreampany.network.misc.length
import com.dreampany.network.misc.secondOrNull
import com.dreampany.network.nearby.core.Packets.Companion.isData
import com.dreampany.network.nearby.core.Packets.Companion.isId
import com.dreampany.network.nearby.core.Packets.Companion.isPeer
import com.google.common.hash.Hashing
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by roman on 7/9/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Packets {
    companion object {
        private const val TYPE_PEER: Byte = 1
        private const val TYPE_DATA: Byte = 2
        private const val TYPE_FILE: Byte = 3

        private const val SUBTYPE_ID: Byte = 1
        private const val SUBTYPE_HASH: Byte = 2
        private const val SUBTYPE_META: Byte = 3
        private const val SUBTYPE_OKAY: Byte = 4

        private const val SUBTYPE_FILE_PAYLOAD_ID: Byte = 6
        private const val SUBTYPE_META_REQUEST: Byte = 7
        private const val SUBTYPE_DATA_REQUEST: Byte = 8

        val ByteArray?.isPeer: Boolean get() {
            val type = this?.firstOrNull()
            return type == TYPE_PEER
        }

        val ByteArray?.isData: Boolean get() {
            val type = this?.firstOrNull()
            return type == TYPE_DATA
        }

        val ByteArray?.isFile: Boolean get() {
            val type = this?.firstOrNull()
            return type == TYPE_FILE
        }

        val ByteArray?.isId: Boolean get() {
            val subtype = this?.secondOrNull()
            return subtype == SUBTYPE_ID
        }

        val ByteArray?.isHash: Boolean get() {
            val subtype = this?.secondOrNull()
            return subtype == SUBTYPE_HASH
        }

        val ByteArray?.isMeta: Boolean get() {
            val subtype = this?.secondOrNull()
            return subtype == SUBTYPE_META
        }

        val ByteArray?.isOkay: Boolean get() {
            val subtype = this?.secondOrNull()
            return subtype == SUBTYPE_OKAY
        }

        val hash256: String
            get() = UUID.randomUUID().toString()


        val ByteArray?.hash256: String
            get() {
                if (this == null || isEmpty) return STRING_EMPTY
                return Hashing.sha256().newHasher().putBytes(this).hash().toString()
            }

        val String?.hash256: String
            get() = this?.toByteArray().hash256

        val ByteArray?.hash256ToLong: Long
            get() {
                if (this == null || isEmpty) return 0L
                return Hashing.sha256().newHasher().putBytes(this).hash().asLong()
            }

        val Long.peerHashPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + 8)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_HASH)
                buf.putLong(this)
                return buf.array()
            }

        val ByteArray?.peerMetaPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + length)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_META)
                this?.let { buf.put(this) }
                return buf.array()
            }

        val peerOkayPacket: ByteArray = ByteBuffer.allocate(1 + 1)
            .put(TYPE_PEER)
            .put(SUBTYPE_OKAY)
            .array()

        val ByteArray.dataPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + size)
                buf.put(TYPE_DATA)
                buf.put(this)
                return buf.array()
            }

        fun copy(src: ByteArray, from: Int): ByteArray {
            return Arrays.copyOfRange(src, from, src.size)
        }

        fun copyToBuffer(src: ByteArray, from: Int): ByteBuffer {
            val data = copy(src, from)
            return ByteBuffer.wrap(data)
        }
    }
}