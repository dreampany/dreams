package com.dreampany.framework.api.broadcast

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CastManager {
    companion object {
        fun castLocally(context: Context, action:String, extra: Map<String, String>) {
            val intent = Intent(action)
            extra.forEach{
                intent.putExtra(it.key, it.value)
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}