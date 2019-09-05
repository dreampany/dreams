package com.dreampany.framework.misc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by Hawladar Roman on 6/1/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

fun <T: ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}