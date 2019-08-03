package com.dreampany.tools.data.misc

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.FileUtil
import com.dreampany.tools.data.model.Apk
import com.dreampany.tools.misc.ApkAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ApkMapper @Inject constructor(
    @ApkAnnote val map: SmartMap<String, Apk>,
    @ApkAnnote val cache: SmartCache<String, Apk>
)  : MediaMapper {

    override fun getUri(): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProjection(): Array<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSelection(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSelectionArgs(): Array<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSortOrder(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isExists(item: Apk): Boolean {
        return map.contains(item.id)
    }

    fun toItem(input: ApplicationInfo?, pm: PackageManager): Apk? {
        if (input == null) {
            return null
        }
        val id = input.packageName
        var out: Apk? = map.get(id)
        if (out == null) {
            out = Apk()
            map.put(id, out)
        }
        out.id = id
        out.name = input.loadLabel(pm).toString()
        out.uri = input.publicSourceDir
        out.mimeType = FileUtil.getMimeType(input.publicSourceDir)
        out.size = FileUtil.getFileSize(input.publicSourceDir)
        return out
    }
}