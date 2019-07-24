/*
package com.dreampany.word.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import android.os.Parcel;

import com.dreampany.frame.data.model.BaseParcel;
import com.dreampany.word.misc.Constants;
import com.google.common.base.Objects;
import com.google.firebase.firestore.PropertyName;

*/
/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

public class Definition extends BaseParcel {

    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    @PropertyName(Constants.Word.PART_OF_SPEECH)
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

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    public void setPartOfSpeech(@NonNull String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    @NonNull
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @NonNull
    public String getText() {
        return text;
    }
}*/
