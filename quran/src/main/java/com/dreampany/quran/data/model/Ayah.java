package com.dreampany.quran.data.model;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.TypeConverters;

import com.dreampany.frame.data.enums.Language;
import com.dreampany.frame.data.model.Base;
import com.dreampany.frame.data.source.room.LanguageConverter;
import com.dreampany.quran.misc.Constants;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by Roman-372 on 5/7/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Entity(indices = {@Index(value = {Constants.Ayah.NUMBER, Constants.Ayah.NUMBER_OF_SURAH}, unique = true)},
        primaryKeys = {Constants.Ayah.NUMBER, Constants.Ayah.NUMBER_OF_SURAH})
@IgnoreExtraProperties
public class Ayah extends Base {

    @PropertyName(Constants.Ayah.NUMBER)
    private int number;
    @PropertyName(Constants.Ayah.NUMBER_OF_SURAH)
    private int numberOfSurah;
    @PropertyName(Constants.Ayah.NUMBER_IN_SURAH)
    private int numberInSurah;
    private String text;
    @TypeConverters(LanguageConverter.class)
    private Language language;
    private String languageText;
    @PropertyName(Constants.Ayah.LOCAL_AUDIO_URL)
    private String localAudioUrl;
    @PropertyName(Constants.Ayah.REMOTE_AUDIO_URL)
    private String remoteAudioUrl;
    private int juz;
    private int manzil;
    private int page;
    private int ruku;
    @PropertyName(Constants.Ayah.HIZB_QUARTER)
    private int hizbQuarter;
    private boolean sajda;

    @Ignore
    public Ayah() {

    }

    public Ayah(int number, int numberOfSurah) {
        this.number = number;
        this.numberOfSurah = numberOfSurah;
    }

    @Ignore
    private Ayah(Parcel in) {
        super(in);
        number = in.readInt();
        numberOfSurah = in.readInt();
        numberInSurah = in.readInt();
        language = in.readParcelable(Language.class.getClassLoader());
        text = in.readString();
        localAudioUrl = in.readString();
        remoteAudioUrl = in.readString();
        juz = in.readInt();
        manzil = in.readInt();
        page = in.readInt();
        ruku = in.readInt();
        hizbQuarter = in.readInt();
        sajda = in.readByte() == 1;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(number);
        dest.writeInt(numberOfSurah);
        dest.writeInt(numberInSurah);
        dest.writeParcelable(language, flags);
        dest.writeString(text);
        dest.writeString(localAudioUrl);
        dest.writeString(remoteAudioUrl);
        dest.writeInt(juz);
        dest.writeInt(manzil);
        dest.writeInt(page);
        dest.writeInt(ruku);
        dest.writeInt(hizbQuarter);
        dest.writeByte((byte) (sajda ? 1 : 0));
    }

    public static final Creator<Ayah> CREATOR = new Creator<Ayah>() {
        @Override
        public Ayah createFromParcel(Parcel in) {
            return new Ayah(in);
        }

        @Override
        public Ayah[] newArray(int size) {
            return new Ayah[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @PropertyName(Constants.Ayah.NUMBER_OF_SURAH)
    public void setNumberOfSurah(int numberOfSurah) {
        this.numberOfSurah = numberOfSurah;
    }

    @PropertyName(Constants.Ayah.NUMBER_OF_SURAH)
    public int getNumberOfSurah() {
        return numberOfSurah;
    }

    @PropertyName(Constants.Ayah.NUMBER_IN_SURAH)
    public void setNumberInSurah(int numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    @PropertyName(Constants.Ayah.NUMBER_IN_SURAH)
    public int getNumberInSurah() {
        return numberInSurah;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @PropertyName(Constants.Ayah.LOCAL_AUDIO_URL)
    public void setLocalAudioUrl(String localAudioUrl) {
        this.localAudioUrl = localAudioUrl;
    }

    @PropertyName(Constants.Ayah.LOCAL_AUDIO_URL)
    public String getLocalAudioUrl() {
        return localAudioUrl;
    }

    @PropertyName(Constants.Ayah.REMOTE_AUDIO_URL)
    public void setRemoteAudioUrl(String remoteAudioUrl) {
        this.remoteAudioUrl = remoteAudioUrl;
    }

    @PropertyName(Constants.Ayah.REMOTE_AUDIO_URL)
    public String getRemoteAudioUrl() {
        return remoteAudioUrl;
    }

    public void setJuz(int juz) {
        this.juz = juz;
    }

    public int getJuz() {
        return juz;
    }

    public void setManzil(int manzil) {
        this.manzil = manzil;
    }

    public int getManzil() {
        return manzil;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setRuku(int ruku) {
        this.ruku = ruku;
    }

    public int getRuku() {
        return ruku;
    }

    @PropertyName(Constants.Ayah.HIZB_QUARTER)
    public void setHizbQuarter(int hizbQuarter) {
        this.hizbQuarter = hizbQuarter;
    }

    @PropertyName(Constants.Ayah.HIZB_QUARTER)
    public int getHizbQuarter() {
        return hizbQuarter;
    }

    public void setSajda(boolean sajda) {
        this.sajda = sajda;
    }

    public boolean isSajda() {
        return sajda;
    }
}
