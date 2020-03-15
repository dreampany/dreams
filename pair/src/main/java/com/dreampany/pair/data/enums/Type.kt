package com.dreampany.pair.data.enums

import com.dreampany.common.data.enums.BaseType
import kotlinx.android.parcel.Parcelize
/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Type : BaseType {
    DEFAULT, MORE, SITE, APP, SCAN,
    NOTE, CRYPTO, HISTORY, WORD,
    NOTEBOOK, OCR, QUIZ, VPN, RADIO,
    STATION, SERVER, CALL, ENGLISH,
    COIN, TRADE, EXCHANGE, BLOCK, CONTACT, FEATURE,
    TODO, COUNTRY, RESUME, QUESTION, LOCK
}