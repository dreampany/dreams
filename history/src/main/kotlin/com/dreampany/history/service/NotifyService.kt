package com.dreampany.history.service

import com.dreampany.history.vm.NotifyViewModel
import com.dreampany.frame.api.service.BaseJobService
import com.firebase.jobdispatcher.JobParameters
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyService : BaseJobService() {

    @Inject
    internal lateinit var vm: NotifyViewModel

    override fun doJob(job: JobParameters): Boolean {
        vm.notifyIf()
        return true
    }

    override fun done(job: JobParameters): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}