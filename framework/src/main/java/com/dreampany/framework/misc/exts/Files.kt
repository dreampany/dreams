package com.dreampany.framework.misc.exts

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