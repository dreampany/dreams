package com.dreampany.framework.misc.exts

import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun File.createFile(format: String, extension: String): File =
    File(this, SimpleDateFormat(format, Locale.US).format(currentMillis) + extension)

val String.mimeType: String?
    get() {
        val file = File(this)
        val selectedUri = Uri.fromFile(file)
        val extension: String = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString())
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return mimeType
    }

val String.fileSize: Long
    get() {
        val file = File(this)
        return if (file.exists()) {
            file.length()
        } else -1
    }