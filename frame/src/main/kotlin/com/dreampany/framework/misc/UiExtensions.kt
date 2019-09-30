package com.dreampany.framework.misc

import androidx.fragment.app.Fragment

/**
 * Created by roman on 2019-09-27
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun Fragment?.resolveText(text: String? = Constants.Default.NULL): String {
    return if (!text.isNullOrEmpty()) text else Constants.Default.STRING
}