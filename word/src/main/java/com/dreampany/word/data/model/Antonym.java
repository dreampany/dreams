package com.dreampany.word.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import android.os.Parcel;

import com.dreampany.frame.data.model.Base;
import com.dreampany.word.misc.Constants;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {Constants.Word.LEFTER, Constants.Word.RIGHTER}, unique = true)},
        primaryKeys = {Constants.Word.LEFTER, Constants.Word.RIGHTER})
public class Antonym extends Base {

    @NonNull
    @ColumnInfo(name = Constants.Word.LEFTER)
    private String left;
    @NonNull
    @ColumnInfo(name = Constants.Word.RIGHTER)
    private String right;

    public Antonym(@NonNull String left, @NonNull String right) {
        if (left.compareTo(right) > 0) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
    }

    private Antonym(Parcel in) {
        super(in);
        left = in.readString();
        right = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(left);
        dest.writeString(right);
    }

    public static final Creator<Antonym> CREATOR = new Creator<Antonym>() {
        @Override
        public Antonym createFromParcel(Parcel in) {
            return new Antonym(in);
        }

        @Override
        public Antonym[] newArray(int size) {
            return new Antonym[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Antonym item = (Antonym) in;
        return Objects.equal(left, item.left) && Objects.equal(right, item.right);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left, right);
    }

    @NonNull
    public String getLeft() {
        return left;
    }

    @NonNull
    public String getRight() {
        return right;
    }
}
