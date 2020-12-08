package com.dreampany.hello.data.source.api

import com.dreampany.hello.data.model.User

/**
 * Created by roman on 25/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface UserDataSource {

    @Throws
    suspend fun write(input: User): Long

    @Throws
    suspend fun read(id: String): User?
}