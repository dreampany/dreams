package com.dreampany.tools.ui.vm

import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.util.NumberUtil
import com.dreampany.tools.ui.misc.ContactRequest
import com.dreampany.tools.ui.misc.WordRequest
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 2019-11-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class BlockViewModel
@Inject constructor(
    private val ex: AppExecutor,
    private val storeRepo: StoreRepository
)
{
    fun blockContactIf(phone: String) {
        ex.postToDisk(Runnable {

        })
    }
}