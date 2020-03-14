package com.dreampany.pair.ui.auth.vm

import android.app.Application
import com.dreampany.common.misc.func.AppExecutor
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.common.ui.vm.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RegistrationViewModel
@Inject constructor(
    application: Application,
    ex: AppExecutor,
    rx: RxMapper,
    rm: ResponseMapper
) : BaseViewModel<Any>(application, ex, rx, rm) {


    fun register() {
        uiScope.launch {

        }
    }


}