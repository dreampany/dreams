package com.dreampany.common.misc.util

import android.webkit.MimeTypeMap

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MediaUtil  {
    companion object {
        fun getMimeType(url: String, defaultMimeType: String): String? {
            var type: String? = defaultMimeType
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }
    }
}