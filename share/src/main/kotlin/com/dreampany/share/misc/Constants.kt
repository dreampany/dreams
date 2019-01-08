package com.dreampany.share.misc

import com.dreampany.media.data.enums.ItemSubtype
import com.dreampany.media.data.enums.ItemType
import com.dreampany.share.data.enums.ItemState
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 8/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
object Constants {
    object Type {
        val MEDIA = ItemType.MEDIA.name
    }

    object Subtype {
        val Apk = ItemSubtype.APK.name
        val Image = ItemSubtype.IMAGE.name
    }

    object State {
        val Shared = ItemState.SHARED.name
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }
}