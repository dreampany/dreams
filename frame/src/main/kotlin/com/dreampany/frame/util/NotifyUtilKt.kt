package com.dreampany.frame.util

import android.app.Activity
import android.view.Gravity
import com.onurkaganaldemir.ktoastlib.KToast

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyUtilKt {
    companion object {
        fun showInfo(activity: Activity, info: String) {
            KToast.successToast(activity, info, Gravity.BOTTOM, KToast.LENGTH_AUTO);
        }
    }
}