package com.dreampany.frame.api.service

import android.content.Context
import com.dreampany.frame.util.JobUtil
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Job
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass


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

    fun <T : BaseJobService> create(
        tag: String,
        classOfService: KClass<T>,
        startTime: Int,
        delay: Int
    ): Boolean {
        return create(tag, classOfService.java, startTime, delay)
    }

    fun <T : BaseJobService> create(
        tag: String,
        classOfService: Class<T>,
        startTime: Int,
        delay: Int
    ): Boolean {
        Timber.v("Job created")
        val job = JobUtil.create(
            dispatcher,
            tag,
            classOfService,
            startTime,
            delay
        )

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
        cancel(tag)
    }

    fun cancel(tag: String) {
        dispatcher.cancel(tag)
    }
}