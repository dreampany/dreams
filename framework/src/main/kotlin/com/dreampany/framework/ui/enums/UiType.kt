package com.dreampany.framework.ui.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 6/28/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Parcelize
enum class UiType : Parcelable {
    DEFAULT,
    ACTIVITY,
    FRAGMENT
}