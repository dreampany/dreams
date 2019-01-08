package com.dreampany.share.service;

import com.dreampany.frame.api.service.BaseJobService;
import com.dreampany.frame.misc.AppExecutors;
import com.firebase.jobdispatcher.JobParameters;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

/**
 * Created by Hawladar Roman on 8/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NotifyService extends BaseJobService {

    //@Inject
    //NotifyViewModel vm;
    @Inject
    AppExecutors ex;

    @DebugLog
    @Override
    public boolean onStartJob(JobParameters params) {
        //ex.getUiExecutor().execute(() -> completeJob(params));
        new Thread(() -> completeJob(params)).start();
        //ex.getNetworkIO().execute(() -> completeJob(params));
        return true;
    }

    @DebugLog
    @Override
    public boolean onStopJob(JobParameters job) {
       // vm.clear();
        return true;
    }

    public void completeJob(final JobParameters params) {
        try {
            //vm.notifyIf();
        } finally {
            jobFinished(params, true);
        }
    }
}
