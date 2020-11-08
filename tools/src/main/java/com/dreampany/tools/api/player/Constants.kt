package com.dreampany.tools.api.player

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 31/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object Extension {
        const val M3U8 = ".m3u8"
    }

    object Header {
        const val ICY_METADATA = "Icy-MetaData"
        const val ICY_METADATA_OK = "1"
        const val ACCEPT_ENCODING = "Accept-Encoding"
        const val ACCEPT_ENCODING_IDENTITY = "identity"
    }

    object MimeType {
        const val AUDIO_MPEG = "audio/mpeg"
    }

    object ContentType {
        const val APPLE_MPEGURL = "application/vnd.apple.mpegurl"
        const val X_MPEGURL = "application/x-mpegurl"
    }

    object Time {
        fun minuteToMillis(minutes: Long): Long {
            return TimeUnit.MINUTES.toMillis(minutes)
        }
    }
}