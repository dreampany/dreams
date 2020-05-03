package com.dreampany.framework.api.cast

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
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