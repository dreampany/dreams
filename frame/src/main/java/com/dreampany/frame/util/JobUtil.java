package com.dreampany.frame.util;

import com.firebase.jobdispatcher.*;

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
                                                    int delayInSecond,
                                                    int periodInSecond) {
        Job job = dispatcher.newJobBuilder()
                .setService(classOfJob)
                .setTag(tag)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(delayInSecond, delayInSecond + periodInSecond))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }
}
