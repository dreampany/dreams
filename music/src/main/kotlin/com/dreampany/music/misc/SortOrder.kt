package com.dreampany.music.misc

import android.provider.MediaStore


/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
object SortOrder {

    object ArtistSortOrder {
        val ARTIST_A_Z = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
        val ARTIST_Z_A = "$ARTIST_A_Z DESC"
        val ARTIST_NUMBER_OF_SONGS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " DESC"
        val ARTIST_NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS + " DESC"
    }

    object AlbumSortOrder {
        val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        val ALBUM_Z_A = "$ALBUM_A_Z DESC"
        val ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS + " DESC"
        val ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST
        val ALBUM_YEAR = MediaStore.Audio.Albums.FIRST_YEAR + " DESC"
    }

    object SongSortOrder {
        val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        val SONG_Z_A = "$SONG_A_Z DESC"
        val SONG_ARTIST = MediaStore.Audio.Media.ARTIST
        val SONG_ALBUM = MediaStore.Audio.Media.ALBUM
        val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"
        val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"
        val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"
        val SONG_FILENAME = MediaStore.Audio.Media.DATA
    }

    object AlbumSongSortOrder {
        val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        val SONG_Z_A = "$SONG_A_Z DESC"
        val SONG_TRACK_LIST = (MediaStore.Audio.Media.TRACK + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
        val SONG_DURATION = SongSortOrder.SONG_DURATION
        val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"
        val SONG_FILENAME = SongSortOrder.SONG_FILENAME
    }

    object ArtistSongSortOrder {
        val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        val SONG_Z_A = "$SONG_A_Z DESC"
        val SONG_ALBUM = MediaStore.Audio.Media.ALBUM
        val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"
        val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"
        val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"
        val SONG_FILENAME = SongSortOrder.SONG_FILENAME
    }

    object ArtistAlbumSortOrder {
        val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        val ALBUM_Z_A = "$ALBUM_A_Z DESC"
        val ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS + " DESC"
        val ALBUM_YEAR = MediaStore.Audio.Artists.Albums.FIRST_YEAR + " DESC"
    }
}