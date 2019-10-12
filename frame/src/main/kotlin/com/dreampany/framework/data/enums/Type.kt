package com.dreampany.framework.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Type : Parcelable {
    DEFAULT,
    MORE,
    SITE,
    APP,
    SCAN,
    NOTE,
    CRYPTO,
    HISTORY,
    WORD,
    NOTEBOOK,
    OCR,
    QUIZ,
    VPN,
    RADIO,
    STATION
}