package com.dreampany.frame.data.model

import android.os.Parcelable
import com.dreampany.frame.misc.Constants

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Task<T : Parcelable>(
    open var id : String? = Constants.Default.NULL,
    open var input: T? = Constants.Default.NULL,
    open var extra: String? = Constants.Default.NULL
) : BaseParcel() {

}