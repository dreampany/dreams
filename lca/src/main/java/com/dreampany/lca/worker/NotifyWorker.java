package com.dreampany.lca.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import com.dreampany.frame.api.service.BaseWorker;
import com.dreampany.lca.vm.NotifyViewModel;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Roman-372 on 4/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class NotifyWorker extends BaseWorker {

    @Inject
    NotifyViewModel vm;

    public NotifyWorker(@NotNull Context context,
                        @NotNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}
