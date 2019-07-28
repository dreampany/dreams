package com.dreampany.word.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Parcelize
enum class ItemState : Parcelable {
    RAW, FULL, FAVORITE, HISTORY, SYNONYM_QUIZ, ANTONYM_QUIZ
}