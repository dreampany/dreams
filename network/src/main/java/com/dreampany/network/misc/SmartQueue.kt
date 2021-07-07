package com.dreampany.network.misc

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class SmartQueue<T> : LinkedBlockingDeque<T>() {

    fun insertFirst(t: T?): Boolean {
        if (t == null) {
            return false
        }
        while (true) {
            try {
                putFirst(t)
                return false
            } catch (ignored: InterruptedException) {
            }
        }
    }

    fun insertFirstUniquely(t: T?): Boolean {
        if (t == null) {
            return false
        }
        if (contains(t)) {
            remove(t)
        }
        while (true) {
            try {
                putFirst(t)
                return false
            } catch (ignored: InterruptedException) {
            }
        }
    }

    fun insertLast(t: T?): Boolean {
        if (t == null) {
            return false
        }
        while (true) {
            try {
                super.putLast(t)
                return false
            } catch (ignored: InterruptedException) {
            }
        }
    }

    fun insertLastUniquely(t: T?): Boolean {
        if (t == null) {
            return false
        }
        if (contains(t)) {
            remove(t)
        }
        while (true) {
            try {
                super.putLast(t)
                return false
            } catch (ignored: InterruptedException) {
            }
        }
    }

    fun pollFirst(timeout: Long): T? {
        return try {
            super.pollFirst(timeout, TimeUnit.MILLISECONDS)
        } catch (ignored: InterruptedException) {
            null
        }
    }

    override fun takeFirst(): T? {
        return try {
            super.takeFirst()
        } catch (ignored: InterruptedException) {
            null
        }
    }

    override fun pollFirst(): T? {
        return try {
            super.pollFirst()
        } catch (ignored: InterruptedException) {
            null
        }
    }
}