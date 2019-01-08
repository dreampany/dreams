package com.dreampany.word.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

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
public class Antonym extends Base {

    @NonNull
    @ColumnInfo(name = "lefter")
    private String left;
    @NonNull
    @ColumnInfo(name = "righter")
    private String right;

    public Antonym(@NotNull String left, @NotNull String right) {
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

    @NotNull
    public String getLeft() {
        return left;
    }

    @NotNull
    public String getRight() {
        return right;
    }
}
