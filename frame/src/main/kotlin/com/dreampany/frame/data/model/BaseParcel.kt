package com.dreampany.frame.data.model

import android.os.Parcelable

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseParcel : BaseSerial(), Parcelable {

    override fun describeContents() = 0
}