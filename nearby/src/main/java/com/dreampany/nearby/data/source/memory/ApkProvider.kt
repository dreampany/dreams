package com.dreampany.nearby.data.source.memory

import android.content.Context
import com.dreampany.framework.misc.exts.installedApps
import com.dreampany.framework.misc.exts.isValid
import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.source.mapper.ApkMapper
import javax.inject.Inject

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkProvider
@Inject constructor(
    private val context: Context,
    private val mapper: ApkMapper
) : MediaProvider<Apk>() {

    override fun gets(): List<Apk>? {
        val pm = context.packageManager
        val infos = context.installedApps(pm)
        val result = mutableListOf<Apk>()
        infos?.forEach { info ->
            if (info.isValid(pm)) {
                result.add(mapper.get(info, pm))
            }
        }
        return result
    }
}