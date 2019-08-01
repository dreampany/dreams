package com.dreampany.translation.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.translation.misc.Constants
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Translation.ID],
        unique = true
    )],
    primaryKeys = [Constants.Translation.ID]
)
data class TextTranslation(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    override var source: String = Constants.Default.STRING,
    override var target: String = Constants.Default.STRING,
    var input: String = Constants.Default.STRING,
    var output: String = Constants.Default.STRING
) : Translation(time = time, id = id, source = source, target = target) {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        source: String,
        target: String,
        input: String,
        output: String
    ) : this(TimeUtil.currentTime(), id, source, target, input, output) {
    }

/*    @Ignore
    private constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeString(source)
        dest.writeString(target)
        dest.writeString(input)
        dest.writeString(output)
    }

    companion object CREATOR : Parcelable.Creator<TextTranslation> {
        override fun createFromParcel(parcel: Parcel): TextTranslation {
            return TextTranslation(parcel)
        }

        override fun newArray(size: Int): Array<TextTranslation?> {
            return arrayOfNulls(size)
        }
    }*/

/*    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TextTranslation
        return Objects.equal(item.source, source)
                && Objects.equal(item.target, target)
                && Objects.equal(item.input, input)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(source, target, input)
    }*/
}