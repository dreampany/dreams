package com.dreampany.tools.api.wordnik.core

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

data class RequestConfig(
    val method: RequestMethod,
    val path: String,
    val headers: Map<String, String> = mapOf(),
    val query: Map<String, List<String>> = mapOf())