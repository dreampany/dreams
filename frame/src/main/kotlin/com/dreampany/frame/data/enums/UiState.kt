package com.dreampany.frame.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 6/28/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Parcelize
enum class UiState : Parcelable {
    DEFAULT,
    SHOW_PROGRESS,
    HIDE_PROGRESS,
    EMPTY,
    ERROR,
    OFFLINE,
    ONLINE,
    CONTENT,
    EDIT,
    SEARCH,
    EXTRA
}