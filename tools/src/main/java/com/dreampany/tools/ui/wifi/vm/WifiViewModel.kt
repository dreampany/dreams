package com.dreampany.tools.ui.wifi.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.wifi.WifiAction
import com.dreampany.tools.data.enums.wifi.WifiState
import com.dreampany.tools.data.enums.wifi.WifiSubtype
import com.dreampany.tools.data.enums.wifi.WifiType
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.pref.WifiPref
import com.dreampany.tools.data.source.wifi.repo.WifiRepo
import com.dreampany.tools.misc.constants.WifiConstants
import com.dreampany.tools.ui.wifi.model.WifiItem
import kotlinx.coroutines.Dispatchers
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
    private val pref: WifiPref,
    private val repo: WifiRepo
) : BaseViewModel<WifiType, WifiSubtype, WifiState, WifiAction, Wifi, WifiItem, UiTask<WifiType, WifiSubtype, WifiState, WifiAction, Wifi>>(
    application,
    rm
) {

    fun loadWifis(offset: Long, callback: () -> Unit) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Wifi>? = null
            var errors: SmartError? = null
            try {
                result = repo.gets(offset, WifiConstants.Limits.WIFIS, callback)
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

    private fun postError(error: SmartError) {
        postMultiple(
            WifiType.WIFI,
            WifiSubtype.DEFAULT,
            WifiState.DEFAULT,
            WifiAction.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<WifiItem>?) {
        postMultiple(
            WifiType.WIFI,
            WifiSubtype.DEFAULT,
            WifiState.DEFAULT,
            WifiAction.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}
