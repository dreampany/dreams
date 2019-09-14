package com.dreampany.tools.api.wordnik.misc

import com.dreampany.framework.misc.Constants
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 2019-06-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object ApiKey {

        const val WORDNIK_API_KEY_DREAM_DEBUG_1 = "hdjdlr7cbloaa523t8zku86e4mi9k41nat22z3s3krtycq9ep"
        const val WORDNIK_API_KEY_DREAM_DEBUG_2 = "xm53cbxjqkb92uys7w3t8ap0832g81nsz6fbbl9u4uzjrfdr9"
        const val WORDNIK_API_KEY_ROMANBJIT = "5c9a53f4c0e012d4cf5a66115420c073d7da523b9081dff1f"
        const val WORDNIK_API_KEY_IFTENET = "a6714f04f26b9f14e29a920702e0f03dde4b84e98f94fe6fe"
        const val WORDNIK_API_KEY_DREAMPANY = "464b0c5a35f469103f3610840dc061f1c768aa1c223ffa447"
    }

    object Word {
        const val API_KEY = "api_key"
        const val SYNONYM = "synonym"
        const val ANTONYM = "antonym"
        const val SYNONYM_ANTONYM = "synonym,antonym"
    }

    object Delay {
        val WordnikKey = TimeUnit.HOURS.toMillis(1)
    }

    object Limit {
        const val WORDNIK_KEY = 10
    }

    object ResponseCode {
        const val NOT_FOUND = Constants.ResponseCode.NOT_FOUND
    }

    object Decision {
        const val RETRY_ON_NOT_FOUND = false
    }
}