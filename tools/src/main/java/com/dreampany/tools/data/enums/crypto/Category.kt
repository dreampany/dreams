package com.dreampany.tools.data.enums.crypto

import android.os.Parcelable
import com.dreampany.tools.misc.constants.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 11/16/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Category(
    val value: String
) : Parcelable {
    COIN(Constants.Values.Crypto.Category.COIN), TOKEN(Constants.Values.Crypto.Category.TOKEN)
}