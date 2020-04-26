package com.dreampany.common.ui.callback

import com.dreampany.common.data.model.Task

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface Callback {
    fun onTask(task: Task<*, *, *, *, *>)
    fun <T> onItem(item: T)
}