package com.dreampany.framework.api.key

import org.apache.commons.collections4.queue.CircularFifoQueue
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class KeyManager @Inject constructor() {
    private lateinit var queue: CircularFifoQueue<Int>
    private val keys = mutableListOf<String>()
    val length: Int
        get() = keys.size

    fun setKeys(vararg keys: String) {
        queue = CircularFifoQueue(keys.size)
        this.keys.clear()
        this.keys.addAll(keys)
        for (index in 0..this.keys.size - 1) {
            queue.add(index)
        }
    }

    fun forwardKey() {
        queue.add(queue.peek())
    }

    fun getKey(): String {
        return keys.get(queue.peek()!!)
    }
}