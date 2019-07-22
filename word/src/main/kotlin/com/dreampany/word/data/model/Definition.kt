package com.dreampany.word.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.frame.data.model.BaseParcelKt
import com.dreampany.word.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.PropertyName

/**
 * Created by Roman-372 on 7/22/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Definition(
    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    @PropertyName(Constants.Word.PART_OF_SPEECH)
    var partOfSpeech: String?,
    var text: String?
) : BaseParcelKt() {

    constructor() : this("", "") {
    }

    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString()) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(partOfSpeech)
        dest.writeString(text)
    }

    companion object CREATOR : Parcelable.Creator<Definition> {
        override fun createFromParcel(parcel: Parcel): Definition {
            return Definition(parcel)
        }

        override fun newArray(size: Int): Array<Definition?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Definition
        return Objects.equal(item.partOfSpeech, partOfSpeech) && Objects.equal(item.text, text)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(partOfSpeech, text)
    }


}