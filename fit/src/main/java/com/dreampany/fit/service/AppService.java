package com.dreampany.fit.service;

import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.dreampany.frame.api.service.BaseService;

/**
 * Created by Roman-372 on 2/7/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class AppService extends BaseService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

        }
        return super.onStartCommand(intent, flags, startId);
    }
}
