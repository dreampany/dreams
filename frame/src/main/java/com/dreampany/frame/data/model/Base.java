package com.dreampany.frame.data.model;

import android.os.Parcel;

import com.google.common.base.Objects;

import androidx.annotation.NonNull;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class Base extends BaseParcel {

    @NonNull
    protected String id;
    protected long time;

    protected Base() {
        this(0L);
    }

    protected Base(String id) {
        this(id, 0L);
    }

    protected Base(long time) {
        this.time = time;
    }

    protected Base(@NonNull String id, long time) {
        this.id = id;
        this.time = time;
    }

    protected Base(Parcel in) {
        super(in);
        id = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
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

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }
}
