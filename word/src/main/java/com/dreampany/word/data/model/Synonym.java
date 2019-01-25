package com.dreampany.word.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {"lefter", "righter"}, unique = true)},
        primaryKeys = {"lefter", "righter"})
public class Synonym extends Base {

    @NonNull
    @ColumnInfo(name = "lefter")
    private String left;
    @NonNull
    @ColumnInfo(name = "righter")
    private String right;

    public Synonym(@NotNull String left, @NotNull String right) {
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

    @NotNull
    public String getLeft() {
        return left;
    }

    @NotNull
    public String getRight() {
        return right;
    }
}
