package com.dreampany.quran.data.model;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.dreampany.frame.data.enums.Language;
import com.dreampany.frame.data.model.Base;
import com.dreampany.quran.misc.Constants;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman-372 on 5/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Entity(indices = {@Index(value = {Constants.Surah.NUMBER}, unique = true)},
        primaryKeys = {Constants.Surah.NUMBER})
@IgnoreExtraProperties
public class Surah extends Base {

    @PropertyName(Constants.Surah.NUMBER)
    private int number;
    @PropertyName(Constants.Surah.LANGUAGE)
    private Language language;
    private String name;
    private String translatedName;
    private List<Ayah> ayahs;

    @Ignore
    public Surah() {

    }

    public Surah(int number) {
        this.number = number;
    }

    @Ignore
    private Surah(Parcel in) {
        super(in);
        number = in.readInt();
        language = in.readParcelable(Language.class.getClassLoader());
        name = in.readString();
        ayahs = in.createTypedArrayList(Ayah.CREATOR);
/*        if (in.readByte() == 1) {
            ayahs = new ArrayList<>();
            in.readList(ayahs, Ayah.class.getClassLoader());
        } else {
            ayahs = null;
        }*/
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(number);
        dest.writeParcelable(language, flags);
        dest.writeString(name);
        dest.writeTypedList(ayahs);
/*        if (ayahs == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(ayahs);
        }*/
    }

    public static final Creator<Surah> CREATOR = new Creator<Surah>() {
        @Override
        public Surah createFromParcel(Parcel in) {
            return new Surah(in);
        }

        @Override
        public Surah[] newArray(int size) {
            return new Surah[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @PropertyName(Constants.Surah.NUMBER)
    public void setNumber(int number) {
        this.number = number;
    }

    @PropertyName(Constants.Surah.NUMBER)
    public int getNumber() {
        return number;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAyahs(List<Ayah> ayahs) {
        this.ayahs = ayahs;
    }

    public List<Ayah> getAyahs() {
        return ayahs;
    }

    /*other api*/
    public void addAyah(Ayah ayah) {
        if (ayahs == null) {
            ayahs = new ArrayList<>();
        }
        ayahs.add(ayah);
    }
}
