package com.dreampany.history.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.data.model.Link
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.misc.Constants
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.History.ID],
        unique = true
    )],
    primaryKeys = [Constants.History.ID]
)
data class History(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var source: HistorySource? = Constants.Default.NULL,
    var type: HistoryType? = Constants.Default.NULL,
    var day: Int = Constants.Default.INT,
    var month: Int = Constants.Default.INT,
    var year: Int = Constants.Default.INT,
    var text: String? = Constants.Default.NULL,
    var html: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var links: MutableList<Link>? = Constants.Default.NULL

) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(
        id: String = Constants.Default.STRING,
        source: HistorySource? = Constants.Default.NULL,
        type: HistoryType? = Constants.Default.NULL,
        day: Int = Constants.Default.INT,
        month: Int = Constants.Default.INT,
        year: Int = Constants.Default.INT
    ) : this(
        time = TimeUtilKt.currentMillis(),
        id = id,
        source = source,
        type = type,
        day = day,
        month = month,
        year = year
    ) {

    }

/*    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        type = parcel.readParcelable<HistoryType>(HistoryType::class.java.classLoader)
        day = parcel.readInt()
        month = parcel.readInt()
        year = parcel.readInt()
        text = parcel.readString()
        html = parcel.readString()
        url = parcel.readString()
        if (parcel.readByte().compareTo(0x00) == 0) {
            links = null
        } else {
            links = mutableListOf()
            parcel.readList(links as MutableList<Any?>, Link::class.java.classLoader)
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeParcelable(type, flags)
        dest.writeInt(day)
        dest.writeInt(month)
        dest.writeInt(year)
        dest.writeString(text)
        dest.writeString(html)
        dest.writeString(url)
        if (links == null) {
            dest.writeByte(0x00)
        } else {
            dest.writeByte(0x01)
            dest.writeList(links as MutableList<Any?>)
        }
    }

    companion object CREATOR : Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }*/

    override fun toString(): String {
        return "History ($id) == $id"
    }

    fun hasLink(): Boolean {
        return !links.isNullOrEmpty()
    }

    fun getFirstLink(): Link? {
        return links?.first()
    }

    fun getLinkByTitle(title: String): Link? {
        return links?.find { title.equals(it.title) }
    }
}