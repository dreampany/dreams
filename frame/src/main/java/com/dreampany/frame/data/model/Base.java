package com.dreampany.frame.data.model;

import android.os.Parcel;
import androidx.annotation.NonNull;

import com.google.common.base.Objects;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class Base extends BaseParcel {

    protected long id;
    protected long time;

    protected Base() {
        this(0);
    }

    protected Base(long time) {
        this.time = time;
    }

    protected Base(Parcel in) {
        super(in);
        id = in.readLong();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeLong(time);
    }

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Base item = (Base) in;
        return Objects.equal(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }
}
