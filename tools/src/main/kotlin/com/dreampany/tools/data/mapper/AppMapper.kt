package com.dreampany.tools.data.mapper

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.FileUtil
import com.dreampany.tools.data.enums.AppType
import com.dreampany.tools.data.model.App
import com.dreampany.tools.injector.annote.app.AppAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppMapper
@Inject constructor(
    val context: Context,
    @AppAnnote val map: SmartMap<String, App>,
    @AppAnnote val cache: SmartCache<String, App>
) : MediaMapper {

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

    fun isExists(item: App): Boolean {
        return map.contains(item.id)
    }

    fun toItem(input: ApplicationInfo?, pm: PackageManager): App? {
        if (input == null) {
            return null
        }
        val id = input.packageName
        var out: App? = map.get(id)
        if (out == null) {
            out = App()
            map.put(id, out)
        }
        out.id = id
        out.name = input.loadLabel(pm).toString()
        out.uri = input.publicSourceDir

        AndroidUtil.getApplicationIconUri(context, id)?.run {
            out.thumbUri = this.toString()
        }
        out.mimeType = FileUtil.getMimeType(input.publicSourceDir)
        out.size = FileUtil.getFileSize(input.publicSourceDir)

        if (AndroidUtil.isSystemApp(input)) {
            out.appType = AppType.SYSTEM
        } else {
            out.appType = AppType.USER
        }
        out.versionCode = AndroidUtil.getApplicationVersionCode(context, id)
        out.versionName = AndroidUtil.getApplicationVersionName(context, id)
        return out
    }
}