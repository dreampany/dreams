package com.dreampany.lca.service;

import com.dreampany.frame.api.service.BaseJobService;
import com.dreampany.lca.vm.NotifyViewModel;
import com.firebase.jobdispatcher.JobParameters;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 7/22/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class NotifyService extends BaseJobService {

    @Inject
    NotifyViewModel vm;

    @Override
    protected boolean doJob(@NotNull JobParameters job) {
        vm.notifyIf();
        return true;
    }

    @Override
    protected boolean done(@NotNull JobParameters job) {
        vm.clear();
        return true;
    }
}