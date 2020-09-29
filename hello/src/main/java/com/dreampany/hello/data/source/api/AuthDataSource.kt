package com.dreampany.hello.data.source.api

import com.dreampany.hello.data.model.Auth

/**
 * Created by roman on 25/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface AuthDataSource {

    @Throws
    suspend fun write(input: Auth): Long

    @Throws
    suspend fun read(id: String): Auth?

    @Throws
    suspend fun readByEmail(email: String): Auth?
}