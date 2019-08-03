package com.dreampany.tools.data.misc

import android.net.Uri

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface MediaMapper {
    fun getUri(): Uri?

    fun getProjection(): Array<String>?

    fun getSelection(): String?

    fun getSelectionArgs(): Array<String>?

    fun getSortOrder(): String?
}