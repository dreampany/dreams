package com.dreampany.hi.data.source.repo

import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.hi.data.source.api.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class UserRepo @Inject constructor(
    @Remote private val remote: UserDataSource
) : UserDataSource {
}