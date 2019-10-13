package com.dreampany.tools.manager

import android.content.ServiceConnection
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PlayerManager
@Inject constructor() {

    private lateinit var connection: ServiceConnection
}