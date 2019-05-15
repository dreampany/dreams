package com.dreampany.frame.data.model

import com.dreampany.frame.data.enums.Type

/**
 * Created by Roman-372 on 5/15/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class SelectEvent<T : Type>(val eventType: T, val selected: Int, val total: Int) : Event() {
}