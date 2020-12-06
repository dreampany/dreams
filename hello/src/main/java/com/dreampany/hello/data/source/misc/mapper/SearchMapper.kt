package com.dreampany.hello.data.source.misc.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.hello.data.source.misc.pref.Prefs
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class SearchMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
) {
}