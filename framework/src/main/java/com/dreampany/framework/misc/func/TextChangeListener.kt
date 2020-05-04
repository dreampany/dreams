package com.dreampany.framework.misc.func

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by roman on 4/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class TextChangeListener : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
     }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
     }
}