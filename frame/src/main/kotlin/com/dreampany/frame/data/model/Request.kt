package com.dreampany.frame.data.model

/**
 * Created by Roman-372 on 7/5/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Request<T>(var input: T? = null, var important: Boolean = false, var progress: Boolean = false, var favorite: Boolean = false) {
}