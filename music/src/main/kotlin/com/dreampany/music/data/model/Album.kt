package com.dreampany.music.data.model

import android.os.Parcel
import android.os.Parcelable
import com.dreampany.frame.data.model.Base


/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
data class Album(val songs: List<Song>) : Base() {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Song.CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTypedList(songs)
    }

    companion object {


        val CREATOR: Parcelable.Creator<Album> = object : Parcelable.Creator<Album> {
            override fun createFromParcel(`in`: Parcel): Album {
                return Album(`in`)
            }

            override fun newArray(size: Int): Array<Album?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun getId(): Long {
        return getFirstSong().albumId
    }

    fun getTitle(): String {
        return getFirstSong().albumName
    }

    fun getArtistId(): Long {
        return getFirstSong().artistId
    }

    fun getArtistName(): String {
        return getFirstSong().artistName
    }

    fun getYear(): Int {
        return getFirstSong().year
    }

    fun getDateModified(): Long {
        return getFirstSong().dateModified
    }

    fun getSongCount() : Int {
        return songs.size
    }

    fun getFirstSong() : Song {
        if (songs.isEmpty()) return Song.EMPTY_SONG
        return this.songs.get(0)
    }

    override fun describeContents(): Int {
        return 0
    }


}