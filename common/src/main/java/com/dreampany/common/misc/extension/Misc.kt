package com.dreampany.common.misc.extension

import android.os.Looper
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Runnable?.isOnUiThread() : Boolean = Thread.currentThread() === Looper.getMainLooper().getThread()
