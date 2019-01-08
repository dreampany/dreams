package com.dreampany.share.misc

import com.dreampany.media.data.model.Media
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
class Comparators @Inject constructor() {

    val displayNameComparator: Comparator<Media>
    val dateModifiedComparator: Comparator<Media>

    init {
        displayNameComparator = DisplayNameComparator()
        dateModifiedComparator = DateModifiedComparator()
    }

    class DisplayNameComparator : Comparator<Media> {
        override fun compare(leftItem: Media?, rightItem: Media?): Int {
            return leftItem?.displayName?.compareTo(rightItem?.displayName!!)!!
        }
    }

    class DateModifiedComparator : Comparator<Media> {
        override fun compare(leftItem: Media?, rightItem: Media?): Int {
            return ((rightItem?.dateModified ?: 0) - (leftItem?.dateModified ?: 0)).toInt()
        }
    }
}
