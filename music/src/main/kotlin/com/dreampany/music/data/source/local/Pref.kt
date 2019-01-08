package com.dreampany.music.data.source.local

import android.content.Context
import com.dreampany.frame.data.source.local.FramePref
import com.dreampany.music.misc.SortOrder
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
class Pref @Inject constructor(context : Context) : FramePref(context) {

    val SONG_SORT_ORDER = "song_sort_order"

    fun getSongSortOrder(): String? {
        return getPublicString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z)
    }

}