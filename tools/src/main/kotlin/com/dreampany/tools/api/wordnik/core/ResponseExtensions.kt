package com.dreampany.tools.api.wordnik.core

import okhttp3.Response

/**
 * Created by roman on 2019-06-08
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

/**
 * Provides an extension to evaluation whether the response is a 1xx code
 */
val Response.isInformational: Boolean get() = this.code in 100..199

/**
 * Provides an extension to evaluation whether the response is a 3xx code
 */
val Response.isRedirect: Boolean get() = this.code in 300..399

/**
 * Provides an extension to evaluation whether the response is a 4xx code
 */
val Response.isClientError: Boolean get() = this.code in 400..499

/**
 * Provides an extension to evaluation whether the response is a 5xx (Standard) through 999 (non-standard) code
 */
val Response.isServerError: Boolean get() = this.code in 500..999