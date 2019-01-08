package com.dreampany.frame.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id", "type", "subtype", "state"}, unique = true)},
        primaryKeys = {"id", "type", "subtype", "state"})
public class Store extends Base {

    @NonNull
    private String type;
    @NonNull
    private String subtype;
    @NonNull
    private String state;
    @NonNull
    private String data;

    @Ignore
    public Store() {
    }

    @Ignore
    public Store(long id, @NonNull String type, @NonNull String subtype) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
    }

    public Store(long id, @NonNull String type, @NonNull String subtype, @NonNull String state) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
        this.state = state;
    }

    @Ignore
    private Store(Parcel in) {
        super(in);
        type = in.readString();
        subtype = in.readString();
        state = in.readString();
        data = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeString(state);
        dest.writeString(data);
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Store item = (Store) in;
        return Objects.equal(id, item.id)
                && Objects.equal(type, item.type)
                && Objects.equal(subtype, item.subtype)
                && Objects.equal(state, item.state);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, subtype, state);
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }

    public void setData(@NonNull String data) {
        this.data = data;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public String getData() {
        return data;
    }

    public boolean hasProperty(String type, String subtype) {
        return Objects.equal(type, this.type) && Objects.equal(subtype, this.subtype);
    }
}
