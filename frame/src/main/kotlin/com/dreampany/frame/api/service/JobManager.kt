package com.dreampany.frame.api.service

import android.content.Context
import com.dreampany.frame.util.JobUtil
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Job
import hugo.weaving.DebugLog
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 10/8/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
class JobManager @Inject constructor(val context: Context) {

    val dispatcher: FirebaseJobDispatcher

    init {
        dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
    }

    @DebugLog
    fun <T : BaseJobService> create(classOfService: Class<T>, startTime: Int, delay: Int): Boolean {
        val tag = classOfService.simpleName
        val job = JobUtil.create(dispatcher,
                tag,
                classOfService,
                startTime,
                delay)

        if (job == null) {
            Timber.e("Job shouldn't be null")
            return false
        }
        return fire(job)
    }

    fun fire(job: Job): Boolean {
        try {
            dispatcher.mustSchedule(job)
            Timber.v("Job mustSchedule")
            return true
        } catch (error: FirebaseJobDispatcher.ScheduleFailedException) {
            Timber.e(error, "Error in Job Schedule")
            return false
        }
    }

    fun <T : BaseJobService> cancel(classOfT: Class<T>) {
        val tag = classOfT.simpleName
        dispatcher.cancel(tag)
    }
}