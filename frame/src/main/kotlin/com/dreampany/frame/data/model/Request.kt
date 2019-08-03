package com.dreampany.frame.data.model

import com.dreampany.frame.misc.Constants

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Request<T>(
    var input: T? = Constants.Default.NULL,
    var important: Boolean = Constants.Default.BOOLEAN,
    var progress: Boolean = Constants.Default.BOOLEAN,
    var favorite: Boolean = Constants.Default.BOOLEAN
) {
}