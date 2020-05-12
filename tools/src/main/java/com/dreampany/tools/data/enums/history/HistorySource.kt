package com.dreampany.tools.data.enums.history

import android.os.Parcelable
import com.dreampany.framework.data.enums.BaseEnum
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class HistorySource : BaseEnum {
   DEFAULT, WIKIPEDIA;
   override val value: String get() = name
}