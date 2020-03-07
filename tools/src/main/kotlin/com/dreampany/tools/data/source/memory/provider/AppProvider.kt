package com.dreampany.tools.data.source.memory.provider

import android.content.Context
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.tools.data.mapper.AppMapper
import com.dreampany.tools.data.model.App
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppProvider
@Inject constructor(
    val context: Context,
    val mapper: AppMapper
) : MediaProvider<App>() {

    override fun getItems(limit: Long): List<App>? {
        val pm = AndroidUtil.getPackageManager(context)
        val infos = AndroidUtil.getInstalledApps(context, pm)
        val result = mutableListOf<App>()
        infos?.forEach { info ->
            if (result.size >= limit)
                return@forEach

            if (AndroidUtil.isValid(pm!!, info) /*&& !AndroidUtil.isSystemApp(info)*/) {
                mapper.getItem(info, pm)?.run {
                    result.add(this)
                }
            }
        }

        return result
    }
}