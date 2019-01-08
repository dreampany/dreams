package com.dreampany.lca.api.cmc.enums;


import com.annimon.stream.Stream;

import java.io.Serializable;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public enum Currency implements Serializable {

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

    private static Currency[] fiatCurrencies;
    private static Currency[] cryptoCurrencies;
    private final CurrencyType type;

    Currency() {
        this(CurrencyType.FIAT);
    }

    Currency(CurrencyType type) {
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

    public static Currency[] getFiatCurrencies() {
        if (fiatCurrencies == null) {
            fiatCurrencies = Stream.of(values()).filter(Currency::isFiat).toArray(Currency[]::new);
        }
        return fiatCurrencies;
    }

    public static Currency[] getCryptoCurrencies() {
        if (cryptoCurrencies == null) {
            cryptoCurrencies = Stream.of(values()).filter(Currency::isCrypto).toArray(Currency[]::new);
        }
        return cryptoCurrencies;
    }

    enum CurrencyType {
        FIAT,
        CRYPTO
    }
}
