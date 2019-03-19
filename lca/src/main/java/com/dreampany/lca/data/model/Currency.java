package com.dreampany.lca.data.model;

import android.os.Parcel;
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
    private final String symbol;
    private final String name;

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

    public static Currency valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return AUD;
            case 1:
                return BRL;
            case 2:
                return CAD;
            case 3:
                return CHF;
            case 4:
                return CLP;
            case 5:
                return CNY;
            case 6:
                return CZK;
            case 7:
                return DKK;
            case 8:
                return EUR;
            case 9:
                return GBP;
            case 10:
                return HKD;
            case 11:
                return HUF;
            case 12:
                return IDR;
            case 13:
                return ILS;
            case 14:
                return INR;
            case 15:
                return JPY;
            case 16:
                return KRW;
            case 17:
                return MXN;
            case 18:
                return MYR;
            case 19:
                return NOK;
            case 20:
                return NZD;
            case 21:
                return PHP;
            case 22:
                return PKR;
            case 23:
                return PLN;
            case 24:
                return RUB;
            case 25:
                return SEK;
            case 26:
                return SGD;
            case 27:
                return THB;
            case 28:
                return TRY;
            case 29:
                return TWD;
            case 30:
                return ZAR;
            case 31:
                return USD;
            case 32:
                return BTC;
            case 33:
                return ETH;
            case 34:
                return XRP;
            case 35:
                return LTC;
            case 36:
            default:
                return BCH;
        }
    }

    enum CurrencyType {
        FIAT,
        CRYPTO
    }
}
