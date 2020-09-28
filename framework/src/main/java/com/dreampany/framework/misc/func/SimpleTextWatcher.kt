package com.dreampany.framework.misc.func

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by roman on 4/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
    }
}