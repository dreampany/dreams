package com.dreampany.demo.misc

import retrofit2.Response

/**
 * Created by roman on 9/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val Response<Any>.isQuoteExpired: Boolean
    get() = code() == 403