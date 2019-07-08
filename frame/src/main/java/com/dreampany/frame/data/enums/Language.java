package com.dreampany.frame.data.enums;

import android.os.Parcel;

import com.dreampany.frame.misc.Constants;

import java.io.Serializable;

/**
 * Created by Roman-372 on 5/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public enum Language implements Type, Serializable {

    ARABIC(Constants.LanguageCode.ARABIC, Constants.LanguageCountry.ARABIC),
    BENGALI(Constants.LanguageCode.BENGALI, Constants.LanguageCountry.BENGALI),
    FRENCH(Constants.LanguageCode.FRENCH, Constants.LanguageCountry.FRENCH),
    SPANISH(Constants.LanguageCode.SPANISH, Constants.LanguageCountry.SPANISH),
    ENGLISH(Constants.LanguageCode.ENGLISH, Constants.LanguageCountry.ENGLISH);

    private final String code;
    private final String country;

    Language(String code, String country) {
        this.code = code;
        this.country = country;
    }

    @Override
    public boolean equals(Type type) {
        if (Language.class.isInstance(type)) {
            Language item = (Language) type;
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

    public String getCode() {
        return code;
    }

    public String getCountry() {
        return country;
    }

    public static final Creator<Language> CREATOR = new Creator<Language>() {

        @Override
        public Language createFromParcel(Parcel in) {
            return Language.valueOf(in.readInt());
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    public static Language valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return ARABIC;
            case 1:
                return BENGALI;
            case 2:
                return FRENCH;
            case 3:
                return SPANISH;
            case 4:
            default:
                return ENGLISH;
        }
    }
}
