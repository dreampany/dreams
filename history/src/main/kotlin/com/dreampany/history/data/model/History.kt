package com.dreampany.history.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.misc.Constants
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.History.ID],
        unique = true
    )],
    primaryKeys = [Constants.History.ID]
)
class History(
    override var time: Long,
    override var id: String
) : BaseKt() {
    var type: HistoryType? = null
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var text: String? = null
    var html: String? = null
    var url: String? = null
    var links: MutableList<Link>? = null

    @Ignore
    constructor() : this("") {

    }

    constructor(id: String) : this(TimeUtil.currentTime(), id) {

    }

    @Ignore
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
    }

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