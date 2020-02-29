package com.dreampany.tools.api.crypto.model

/**
 * Created by roman on 2019-11-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
enum class CryptoCurrency(val type: Type) {
    AUD(),
    BDT(),
    BRL(),
    CAD(),
    CHF(),
    CLP(),
    CNY(),
    CZK(),
    DKK(),
    EUR(),
    GBP(),
    HKD(),
    HUF(),
    IDR(),
    ILS(),
    INR(),
    JPY(),
    KRW(),
    MXN(),
    MYR(),
    NOK(),
    NZD(),
    PHP(),
    PKR(),
    PLN(),
    RUB(),
    SEK(),
    SGD(),
    THB(),
    TRY(),
    TWD(),
    USD(),
    ZAR(),
    BTC(Type.CRYPTO),
    ETH(Type.CRYPTO),
    XRP(Type.CRYPTO),
    LTC(Type.CRYPTO),
    BCH(Type.CRYPTO);

    constructor() : this(Type.FIAT) {

    }

    enum class Type {
        FIAT, CRYPTO
    }
}