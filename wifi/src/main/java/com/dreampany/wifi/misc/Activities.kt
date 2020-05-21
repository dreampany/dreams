package com.dreampany.wifi.misc

import android.app.Activity
import android.content.Intent

/**
 * Created by roman on 22/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Activity?.open(action: String, requestCode: Int) {
    this?.run {
        val intent = Intent(action)
        startActivityForResult(intent, requestCode)
    }
}