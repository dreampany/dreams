package com.dreampany.network.misc

import com.google.common.collect.Maps
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 7/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class Runner : Runnable {
    protected val delayMilli = TimeUnit.MILLISECONDS.toMillis(10)
    protected val delayS = TimeUnit.SECONDS.toMillis(1)
    protected var wait = delayS
    protected val times: MutableMap<String, Long>
    protected val delays: MutableMap<String, Long>

    @Volatile
    private lateinit var thread: Thread

    @Volatile
    var running = false

    @Volatile
    private var guarded = false
    private val guard = Object()

    init {
        times = Maps.newConcurrentMap()
        delays = Maps.newConcurrentMap()
    }

    @Throws(InterruptedException::class)
    abstract fun looping(): Boolean

    override fun run() {
        try {
            while (running) {
                if (!looping()) {
                    break
                }
            }
        } catch (ignored: InterruptedException) {
        }
        running = false
    }

    fun start() {
        if (running) {
            return
        }
        running = true
        thread = Thread(this)
        thread.isDaemon = true
        thread.start()
    }

    fun stop() {
        if (!running) {
            return
        }
        running = false
        thread.interrupt()
        notifyRunner()
    }

    fun waitRunner(timeoutMs: Long) {
        if (guarded) {
            //return;
        }
        guarded = true
        synchronized(guard) {
            try {
                if (timeoutMs > 0L) {
                    guard.wait(timeoutMs)
                } else {
                    guard.wait()
                }
            } catch (e: InterruptedException) {
            }
        }
    }

    fun notifyRunner() {
        if (!guarded) {
            //return;
        }
        guarded = false
        synchronized(guard) { guard.notify() }
    }

    fun interrupt() {
        if (!::thread.isInitialized || thread.isInterrupted || !thread.isAlive && !thread.isDaemon) {
            return
        }
        thread.interrupt()
    }

    @Throws(InterruptedException::class)
    protected fun waitFor(timeout: Long) = Thread.sleep(timeout)
}