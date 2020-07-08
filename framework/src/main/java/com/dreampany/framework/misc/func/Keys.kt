package com.dreampany.framework.misc.func

import com.dreampany.framework.misc.util.Util
import org.apache.commons.collections4.queue.CircularFifoQueue
import javax.inject.Inject

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Keys
@Inject constructor() {
    private lateinit var queue: CircularFifoQueue<Int>
    private val keys = mutableListOf<String>()

    val length: Int
        get() = keys.size

    val indexLength: Int
        get() = length.dec()

    fun setKeys(vararg keys: String) {
        queue = CircularFifoQueue(keys.size)
        this.keys.clear()
        this.keys.addAll(keys)
        for (index in 0..this.indexLength) {
            queue.add(index)
        }
    }

    fun forwardKey() {
        queue.add(queue.peek())
    }

    fun randomForwardKey() {
        val rand = Util.nextRand((length / 2) + 1)
        for (index in 1..rand) {
            queue.add(queue.peek())
        }
    }

    val nextKey : String?
        get()  {
            val index = queue.peek() ?: return null
            val key = keys.get(index)
            randomForwardKey()
            return key
        }
}