package com.dreampany.word.data.model;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;

import com.dreampany.frame.data.model.BaseParcel;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class Definition extends BaseParcel {

    private String partOfSpeech;
    private String text;

    public Definition() {
    }

    @Ignore
    private Definition(Parcel in) {
        super(in);
        partOfSpeech = in.readString();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(partOfSpeech);
        dest.writeString(text);
    }

    public static final Creator<Definition> CREATOR = new Creator<Definition>() {
        @Override
        public Definition createFromParcel(Parcel in) {
            return new Definition(in);
        }

        @Override
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Definition item = (Definition) in;
        return Objects.equal(partOfSpeech, item.partOfSpeech) && Objects.equal(text, item.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(partOfSpeech, text);
    }

    public void setPartOfSpeech(@NotNull String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @NotNull
    public String getText() {
        return text;
    }
}