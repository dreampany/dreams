package com.dreampany.common.misc.func

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RxFacade
@Inject constructor() {

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun compute(): Scheduler {
        return Schedulers.computation()
    }

/*    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }*/
}