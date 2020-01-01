package com.dreampany.framework.misc

import android.os.Handler
import android.os.HandlerThread

/**
 * Created by Hawladar Roman on 5/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class HandlerManager {
    private var handler: Handler? = null
    private var thread: HandlerThread? = null

    @Synchronized
    fun start() {
        if (thread == null) {
            thread = HandlerThread(HandlerManager::class.java.simpleName)
            thread!!.start()
            handler = Handler(thread!!.looper)
        }
    }

    @Synchronized
    fun stop() {
        if (thread != null) {
            thread!!.quitSafely()
            try {
                thread!!.join()
                thread = null
                handler = null
            } catch (ignored: InterruptedException) {
            }
        }
    }

    @Synchronized
    fun run(runnable: Runnable) {
        if (handler != null) {
            handler!!.post(runnable)
        }
    }

    @Synchronized
    fun run(runnable: Runnable, delay: Long) {
        if (handler != null) {
            handler!!.postDelayed(runnable, delay)
        }
    }
}
