package com.dreampany.manager.service;

import com.dreampany.manager.vm.NotifyViewModel;
import com.dreampany.frame.api.service.BaseJobService;
import com.firebase.jobdispatcher.JobParameters;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Created by Hawladar Roman on 7/22/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class NotifyService extends BaseJobService {

    @Inject
    NotifyViewModel vm;
    @Override
    protected boolean doJob(@NonNull JobParameters job) {
        vm.notifyIf();
        return true;
    }

    @Override
    protected boolean done(@NonNull JobParameters job) {
        vm.clear();
        return true;
    }
}