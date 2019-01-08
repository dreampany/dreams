package com.dreampany.music.data.model

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import com.dreampany.frame.data.model.Base


/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(primaryKeys = ["id"])
data class Song(
        val id: Long,
        val title: String,
        val trackNumber: Int,
        val year: Int,
        val duration: Long,
        val data: String,
        val dateModified: Long,
        val albumId: Long,
        val albumName: String,
        val artistId: Long,
        val artistName: String) : Base() {

    companion object {
        val EMPTY_SONG = Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "")
        val CREATOR: Parcelable.Creator<Song> = object : Parcelable.Creator<Song> {
            override fun createFromParcel(`in`: Parcel): Song {
                return Song(`in`)
            }

            override fun newArray(size: Int): Array<Song?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeInt(trackNumber)
        parcel.writeInt(year)
        parcel.writeLong(duration)
        parcel.writeString(data)
        parcel.writeLong(dateModified)
        parcel.writeLong(albumId)
        parcel.writeString(albumName)
        parcel.writeLong(artistId)
        parcel.writeString(artistName)
    }

    override fun describeContents(): Int {
        return 0
    }



/*    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }*/


}