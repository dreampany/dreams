package com.dreampany.music.data.model

import android.os.Parcel
import android.os.Parcelable
import com.dreampany.frame.data.model.Base
import com.dreampany.music.util.MusicUtil


/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
data class Artist(val albums: List<Album>) : Base() {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Album.CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTypedList(albums)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Artist> = object : Parcelable.Creator<Artist> {
            override fun createFromParcel(`in`: Parcel): Artist {
                return Artist(`in`)
            }

            override fun newArray(size: Int): Array<Artist?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun getId(): Long {
        return getFirstAlbum().getArtistId()
    }

/*    fun getName(): String? {
        val name = getFirstAlbum().getArtistName()
        return if (MusicUtil.isArtistNameUnknown(name)) {
            return null
        } else name
    }*/

    fun getSongCount(): Int {
        var songCount = 0
        for (album in albums) {
            songCount += album.getSongCount()
        }
        return songCount
    }

    fun getAlbumCount(): Int {
        return albums.size
    }

    fun getSongs(): List<Song> {
        val result = ArrayList<Song>()
        for ((songs) in albums) {
            result.addAll(songs)
        }
        return result
    }

    fun getFirstAlbum(): Album {
        return if (albums.isEmpty()) Album(ArrayList()) else albums[0]
    }
}