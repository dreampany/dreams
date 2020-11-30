package com.dreampany.tools.ui.wifi.vm

import android.app.Application
import com.dreampany.framework.misc.func.Executors
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.wifi.WifiAction
import com.dreampany.tools.data.enums.wifi.WifiState
import com.dreampany.tools.data.enums.wifi.WifiSubtype
import com.dreampany.tools.data.enums.wifi.WifiType
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.pref.Prefs
import com.dreampany.tools.data.source.wifi.repo.WifiRepo
import com.dreampany.tools.misc.constants.Constants
import com.dreampany.tools.ui.wifi.model.WifiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val ex: Executors,
    private val pref: Prefs,
    private val repo: WifiRepo
) : BaseViewModel<WifiType, WifiSubtype, WifiState, WifiAction, Wifi, WifiItem, UiTask<WifiType, WifiSubtype, WifiState, WifiAction, Wifi>>(
    application,
    rm
) {

    private var running = false

    fun loadWifis(offset: Long, callback: () -> Unit) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Wifi>? = null
            var errors: SmartError? = null
            try {
                result = repo.gets(offset, Constants.Limits.WIFIS, callback)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun startPeriodicWifis() {
        nextRun(Constants.Times.Wifi.PERIODIC_SCAN)
    }

    fun stopPeriodicWifis() {
        ex.getUiHandler().removeCallbacks(periodicRunner)
        running = false
    }

    private fun nextRun(delay: Long) {
        stopPeriodicWifis()
        ex.getUiHandler().postDelayed(periodicRunner, delay)
        running = true
    }

    private fun periodicWifisInternal() {
        uiScope.launch {
            var result: List<Wifi>? = null
            var errors: SmartError? = null
            try {
                result = repo.gets()
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors, false)
            } else {
                postResult(result?.toItems(), false)
            }
        }
    }

    private val periodicRunner = object : Runnable {
        override fun run() {
            periodicWifisInternal()
            nextRun(Constants.Times.Wifi.PERIODIC_SCAN)
        }
    }

    private suspend fun List<Wifi>.toItems(): List<WifiItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                //val favorite = repo.isFavorite(input)
                WifiItem.get(input, false)
            }
        }
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            WifiType.WIFI,
            WifiSubtype.DEFAULT,
            WifiState.DEFAULT,
            WifiAction.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError, showProgress: Boolean = true) {
        postMultiple(
            WifiType.WIFI,
            WifiSubtype.DEFAULT,
            WifiState.DEFAULT,
            WifiAction.DEFAULT,
            error = error,
            showProgress = showProgress
        )
    }

    private fun postResult(result: List<WifiItem>?, showProgress: Boolean = true) {
        postMultiple(
            WifiType.WIFI,
            WifiSubtype.DEFAULT,
            WifiState.DEFAULT,
            WifiAction.DEFAULT,
            result = result,
            showProgress = showProgress
        )
    }
}
