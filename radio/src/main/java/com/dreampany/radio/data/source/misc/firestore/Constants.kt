package com.dreampany.radio.data.source.misc.firestore

import com.dreampany.framework.misc.constant.Constant

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object Api {
        const val GOOGLE_CLIENT_ID_DREAMPANY_MAIL =
            "Mzg3MTgwMDk4NzI4LXVrMjIyOXA5dDJlMHI5ZmwzODRkNHY3ZzlkNDdhMDJvLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29t"
    }

    object Keys {
        // firestore collection
        const val AUTHS = "hello.auths"
        const val USERS = "users"
        const val SEARCHES = "searches"

        // auth keys
        const val EMAIL = "email"
        const val PASSWORD = "password"

        //search keys
        const val HITS = "hits"
        fun hit(ref: String): String = HITS.plus(Constant.Sep.DOT).plus(ref)
    }
}