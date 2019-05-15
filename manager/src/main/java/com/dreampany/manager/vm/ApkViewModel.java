package com.dreampany.manager.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.manager.ui.model.MediaItem;
import com.dreampany.manager.ui.model.UiTask;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.repository.ApkRepository;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Roman-372 on 5/15/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class ApkViewModel extends BaseViewModel<Apk, MediaItem, UiTask<Apk>> {

    protected ApkViewModel(Application application,
                           RxMapper rx,
                           AppExecutors ex,
                           ResponseMapper rm) {
        super(application, rx, ex, rm);
    }

    //private final ApkRepository repo;
}
