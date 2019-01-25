package com.dreampany.frame.util;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Hawladar Roman on 10/8/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class JobUtil {
    private JobUtil() {}

    public static <T extends JobService> Job create(FirebaseJobDispatcher dispatcher,
                                                    String tag,
                                                    Class<T> classOfJob,
                                                    int startTimeInSecond,
                                                    int delayInSecond) {
        Job job = dispatcher.newJobBuilder()
                .setService(classOfJob)
                .setTag(tag)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(startTimeInSecond, startTimeInSecond + delayInSecond))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }
}
