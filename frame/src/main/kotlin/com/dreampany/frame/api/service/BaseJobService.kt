package com.dreampany.frame.api.service

import android.app.Service
import com.dreampany.frame.misc.AppExecutors
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
abstract class BaseJobService : JobService(), HasServiceInjector {

    @Inject
    internal lateinit var serviceInjector: DispatchingAndroidInjector<Service>
    @Inject
    internal lateinit var ex: AppExecutors

    protected abstract fun doJob(job: JobParameters): Boolean
    protected abstract fun done(job: JobParameters): Boolean

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceInjector
    }

    override fun onStartJob(job: JobParameters): Boolean {
        ex.postToNetwork(Runnable{ completeJob(job) })
        return true // need a good implementation to return true / false
    }

    override fun onStopJob(job: JobParameters): Boolean {
        return done(job)
    }

    private fun completeJob(job: JobParameters): Boolean {
        var success: Boolean
        try {
            success = doJob(job)
        } finally {
            jobFinished(job, true)
        }
        return success
    }
}
