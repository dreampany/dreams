package com.dreampany.fit.util;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.dreampany.fit.service.AppService;

/**
 * Created by Roman-372 on 2/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public final class Util {

    private Util() {
    }

    public static void listenStepCounter(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        Intent service = new Intent(appContext, AppService.class);

    }
}
