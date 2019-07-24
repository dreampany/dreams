package com.dreampany.lca.data.enums;

import android.os.Parcel;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.enums.Type;

import java.io.Serializable;

/**
 * Created by Roman on 1/14/2019
 * Copyright (c) 2019 Dreampany. All rights reserved.
 * dreampanymail@gmail.com
 * Last modified $file.lastModified
 */
public enum Currency implements Type, Serializable {

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
    BTC(CurrencyType.CRYPTO),
    ETH(CurrencyType.CRYPTO),
    XRP(CurrencyType.CRYPTO),
    LTC(CurrencyType.CRYPTO),
    BCH(CurrencyType.CRYPTO);

    private final CurrencyType type;
    private final String symbol;
    private final String name;

    private static Currency[] fiatCurrencies;
    private static Currency[] cryptoCurrencies;

    Currency() {
        this(CurrencyType.FIAT);
    }

    Currency(CurrencyType type) {
        this(type, "", "");
    }

    Currency(CurrencyType type, String symbol, String name) {
        this.type = type;
        this.symbol = symbol;
        this.name = name;
    }

    @Override
    public boolean equals(Type type) {
        if (Currency.class.isInstance(type)) {
            Currency item = (Currency) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String toLowerValue() {
        return name().toLowerCase();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {

        @Override
        public Currency createFromParcel(Parcel in) {
            return Currency.valueOf(in.readInt());
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public boolean isFiat() {
        return type == CurrencyType.FIAT;
    }

    public boolean isCrypto() {
        return type == CurrencyType.CRYPTO;
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

    public static Currency valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return AUD;
            case 1:
                return BDT;
            case 2:
                return BRL;
            case 3:
                return CAD;
            case 4:
                return CHF;
            case 5:
                return CLP;
            case 6:
                return CNY;
            case 7:
                return CZK;
            case 8:
                return DKK;
            case 9:
                return EUR;
            case 10:
                return GBP;
            case 11:
                return HKD;
            case 12:
                return HUF;
            case 13:
                return IDR;
            case 14:
                return ILS;
            case 15:
                return INR;
            case 16:
                return JPY;
            case 17:
                return KRW;
            case 18:
                return MXN;
            case 19:
                return MYR;
            case 20:
                return NOK;
            case 21:
                return NZD;
            case 22:
                return PHP;
            case 23:
                return PKR;
            case 24:
                return PLN;
            case 25:
                return RUB;
            case 26:
                return SEK;
            case 27:
                return SGD;
            case 28:
                return THB;
            case 29:
                return TRY;
            case 30:
                return TWD;
            case 31:
                return USD;
            case 32:
                return ZAR;
            case 33:
                return BTC;
            case 34:
                return ETH;
            case 35:
                return XRP;
            case 36:
                return LTC;
            case 37:
            default:
                return BCH;
        }
    }

   public enum CurrencyType {
        FIAT,
        CRYPTO
    }
}
