package com.dreampany.tools.data.source.memory.provider

import android.content.Context
import android.content.pm.PackageManager
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.tools.data.misc.ApkMapper
import com.dreampany.tools.data.model.Apk
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ApkProvider constructor(
    val context: Context,
    val mapper: ApkMapper
) : MediaProvider<Apk>() {

    override fun getItems(limit: Int): List<Apk>? {
        val pm = context.applicationContext.packageManager
        val infos = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val result = mutableListOf<Apk>()
        infos.forEach { info ->
            if (result.size >= limit) {
                return@forEach
            }
            if (AndroidUtil.isValid(pm, info) /*&& !AndroidUtil.isSystemApp(info)*/) {
                mapper.toItem(info, pm)?.run {
                    result.add(this)
                }
            }
        }
        return result
    }
}