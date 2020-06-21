package com.dreampany.nearby.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.nearby.data.enums.*
import com.dreampany.nearby.data.model.User
import com.dreampany.nearby.ui.home.model.UserItem
import javax.inject.Inject

/**
 * Created by roman on 21/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UserViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: AppPref,
    private val repo: CoinRepo
) : BaseViewModel<Type, Subtype, State, Action, User, UserItem, UiTask<Type, Subtype, State, Action, User>>(
    application,
    rm
) {
}