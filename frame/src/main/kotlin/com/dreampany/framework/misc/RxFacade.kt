package com.dreampany.framework.misc

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
class RxFacade @Inject constructor() {

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun compute(): Scheduler {
        return Schedulers.computation()
    }

    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}