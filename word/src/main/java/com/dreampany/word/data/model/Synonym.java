package com.dreampany.word.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.dreampany.word.misc.Constants;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {Constants.Synonym.LEFTER, Constants.Synonym.RIGHTER}, unique = true)},
        primaryKeys = {Constants.Synonym.LEFTER, Constants.Synonym.RIGHTER})
public class Synonym extends Base {

    @NonNull
    @ColumnInfo(name = Constants.Synonym.LEFTER)
    private String left;
    @NonNull
    @ColumnInfo(name = Constants.Synonym.RIGHTER)
    private String right;

    public Synonym(@NonNull String left, @NonNull String right) {
        if (left.compareTo(right) > 0) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
    }

    private Synonym(Parcel in) {
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

    public static final Creator<Synonym> CREATOR = new Creator<Synonym>() {
        @Override
        public Synonym createFromParcel(Parcel in) {
            return new Synonym(in);
        }

        @Override
        public Synonym[] newArray(int size) {
            return new Synonym[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Synonym item = (Synonym) in;
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
