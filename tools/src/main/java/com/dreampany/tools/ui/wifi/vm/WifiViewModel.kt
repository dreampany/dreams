package com.dreampany.tools.ui.wifi.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tools.data.enums.wifi.WifiAction
import com.dreampany.tools.data.enums.wifi.WifiState
import com.dreampany.tools.data.enums.wifi.WifiSubtype
import com.dreampany.tools.data.enums.wifi.WifiType
import com.dreampany.tools.data.source.wifi.pref.WifiPref
import com.dreampany.tools.ui.wifi.model.WifiItem
import com.dreampany.wifi.data.model.Wifi
import javax.inject.Inject

/**
 * Created by roman on 23/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiViewModel@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: WifiPref
) : BaseViewModel<WifiType, WifiSubtype, WifiState, WifiAction, Wifi, WifiItem, UiTask<WifiType, WifiSubtype, WifiState, WifiAction, Wifi>>(
    application,
    rm
) {
}