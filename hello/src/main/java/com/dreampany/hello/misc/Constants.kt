package com.dreampany.hello.misc

import com.dreampany.framework.misc.constant.Constant
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object Apis {
        const val GOOGLE_CLIENT_ID_DREAMPANY_MAIL =
            "Mzg3MTgwMDk4NzI4LXVrMjIyOXA5dDJlMHI5ZmwzODRkNHY3ZzlkNDdhMDJvLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29t"
    }

    object Keys {
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0

        object Pref {
            const val MISC = "misc"
            const val PREF = "pref"
            const val EXPIRE = "expire"
            const val STARTED = "started"
            const val LOGGED = "logged"
            const val SIGN_IN = "sign_in"
            const val AUTH = "auth"
            const val USER = "user"
        }

        object Room {
            const val MISC = "misc"
            const val ROOM = "room"
        }

        object Firestore {
            // firestore collection
            const val AUTHS = "auths"
            const val USERS = "users"
            const val SEARCHES = "searches"

            // auth keys
            const val TIME = Constant.Keys.TIME
            const val ID = Constant.Keys.ID
            const val REF = "ref"
            const val USERNAME = "username"
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val NAME = "name"
            const val PHOTO = "photo"
            const val BIRTHDAY = "birthday"
            const val GENDER = "gender"
            const val COUNTRY = "gender"
            const val CODE = "code"
            const val FLAG = "flag"
            const val PHONE = "phone"
            const val TYPE = "type"
            const val REGISTERED = "registered"
            const val VERIFIED = "verified"
            const val LOGGED = "logged"

            //search keys
            const val HITS = "hits"
            fun hit(ref: String): String = HITS.plus(Constant.Sep.DOT).plus(ref)
        }

        object Country {
            const val TIME = "country_time"
            const val ID = "country_id"
            const val NAME = "country_name"
            const val FLAG = "country_flag"
        }
    }

    object Times {
        val HOUSE_ADS = TimeUnit.DAYS.toMillis(1)
        val MIN_BIRTH = TimeUnit.DAYS.toMillis(16 * 365)
    }

    object Date {
        const val MIN_AGE = 16
    }

    object Pattern {
        const val YY_MM_DD: String = "yy/MM/dd"
    }

/*    object AuthType {
        const val EMAIL = "email"
        const val GOOGLE = "google"
        const val FACEBOOK = "facebook"
    }

    object Gender {
        const val MALE = "male"
        const val FEMALE = "female"
        const val OTHER = "other"
    }*/
}