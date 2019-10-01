package com.dreampany.lca.data.enums

import android.os.Parcelable
import com.annimon.stream.Stream
import com.annimon.stream.function.Predicate
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class CoinSource : Parcelable {
    CMC, CC
}