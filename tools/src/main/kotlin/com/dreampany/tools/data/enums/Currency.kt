package com.dreampany.tools.data.enums

import android.os.Parcelable
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-01
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Currency(
    val type: Type,
    var symbol: String? = Constants.Default.NULL,
    var value: String? = Constants.Default.NULL
) : Parcelable {

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

    @Parcelize
    enum class Type : Parcelable {
        FIAT, CRYPTO
    }

    constructor() : this(Type.FIAT) {

    }

    fun isFiat(): Boolean {
        return type == Type.FIAT
    }

    fun isCrypto(): Boolean {
        return type == Type.CRYPTO
    }

    companion object {
        private var fiats: Array<Currency>? = null
        private var cryptos: Array<Currency>? = null

        fun getFiats(): Array<Currency> {
            if (fiats == null) {
                fiats = values().filter { it.isFiat() }.toTypedArray()
            }
            return fiats!!
        }

        fun getCryptos(): Array<Currency> {
            if (cryptos == null) {
                cryptos = values().filter { it.isCrypto() }.toTypedArray()
            }
            return cryptos!!
        }
    }
}