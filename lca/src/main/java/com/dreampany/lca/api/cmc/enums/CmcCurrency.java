package com.dreampany.lca.api.cmc.enums;


import com.annimon.stream.Stream;

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

    private static CmcCurrency[] fiatCurrencies;
    private static CmcCurrency[] cryptoCurrencies;
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

    public boolean isFiat() {
        return CurrencyType.FIAT == this.type;
    }

    public boolean isCrypto() {
        return CurrencyType.CRYPTO == this.type;
    }

    public static CmcCurrency[] getFiatCurrencies() {
        if (fiatCurrencies == null) {
            fiatCurrencies = Stream.of(values()).filter(CmcCurrency::isFiat).toArray(CmcCurrency[]::new);
        }
        return fiatCurrencies;
    }

    public static CmcCurrency[] getCryptoCurrencies() {
        if (cryptoCurrencies == null) {
            cryptoCurrencies = Stream.of(values()).filter(CmcCurrency::isCrypto).toArray(CmcCurrency[]::new);
        }
        return cryptoCurrencies;
    }

    enum CurrencyType {
        FIAT,
        CRYPTO
    }
}
