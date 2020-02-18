package com.dreampany.framework.data.model

import android.os.Parcelable
import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Task<T : Parcelable>(
    open var id : String? = Constants.Default.NULL,
    open var input: T? = Constants.Default.NULL,
    open var inputs: ArrayList<T>? = Constants.Default.NULL,
    open var extra: String? = Constants.Default.NULL
) : BaseParcel() {

}