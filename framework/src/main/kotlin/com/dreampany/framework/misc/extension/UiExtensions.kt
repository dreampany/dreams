package com.dreampany.framework.misc.extension

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-09-27
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun EditText?.isEmpty(): Boolean {
    return this?.text?.trim().isNullOrEmpty() ?: false
}

fun EditText?.rawText(): String? {
    return this?.text?.trim().toString()
}

fun Fragment?.resolveText(text: String? = Constants.Default.NULL): String {
    return if (!text.isNullOrEmpty()) text else Constants.Default.STRING
}