package com.dreampany.lca.api.cmc.enums;


import java.io.Serializable;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public enum CmcCurrency implements Serializable {
    AUD(),
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
    ZAR(),
    USD(),
    BTC(CurrencyType.CRYPTO),
    ETH(CurrencyType.CRYPTO),
    XRP(CurrencyType.CRYPTO),
    LTC(CurrencyType.CRYPTO),
    BCH(CurrencyType.CRYPTO);

    private final CurrencyType type;

    CmcCurrency() {
        this(CurrencyType.FIAT);
    }

    CmcCurrency(CurrencyType type) {
        this.type = type;
    }

    public CurrencyType getType() {
        return type;
    }

    enum CurrencyType {
        FIAT,
        CRYPTO
    }
}
