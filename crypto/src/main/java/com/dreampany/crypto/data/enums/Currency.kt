package com.dreampany.crypto.data.enums

import android.os.Parcelable
import com.dreampany.framework.misc.constant.Constant
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
    val symbol: String = Constant.Default.STRING,
    val value: String = Constant.Default.STRING
) : Parcelable {

    AED(),
    ARS(),
    AUD(),
    BDT(),
    BHD(),
    BMD(),
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
    BNB(Type.CRYPTO),
    EOS(Type.CRYPTO),
    BTC(Type.CRYPTO),
    BCH(Type.CRYPTO),
    ETH(Type.CRYPTO),
    LTC(Type.CRYPTO),
    XLM(Type.CRYPTO),
    XRP(Type.CRYPTO);

    @Parcelize
    enum class Type : Parcelable {
        FIAT, CRYPTO
    }

    constructor() : this(Type.FIAT) {

    }

    val isFiat: Boolean get() = type == Type.FIAT

    val isCrypto: Boolean get() = type == Type.CRYPTO

    companion object {
        private var fiats: Array<Currency>? = null
        private var cryptos: Array<Currency>? = null

        fun getFiats(): Array<Currency> {
            if (fiats == null) {
                fiats = values().filter { it.isFiat }.toTypedArray()
            }
            return fiats!!
        }

        fun getCryptos(): Array<Currency> {
            if (cryptos == null) {
                cryptos = values().filter { it.isCrypto }.toTypedArray()
            }
            return cryptos!!
        }
    }
}