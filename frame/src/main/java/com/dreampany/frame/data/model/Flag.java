/*
package com.dreampany.frame.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.google.common.base.Objects;

*/
/**
 * Created by Hawladar Roman on 3/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

@Entity(indices = {@Index(value = {"id", "type", "subtype"}, unique = true)},
        primaryKeys = {"id", "type", "subtype"})
public class Flag extends Base {

    @NonNull
    private String type;
    @NonNull
    private String subtype;
    private int orderBy;

    @Ignore
    public Flag() {
    }

    public Flag(long id, @NonNull String type, @NonNull String subtype) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
    }

    @Ignore
    private Flag(Parcel in) {
        super(in);
        type = in.readString();
        subtype = in.readString();
        orderBy = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeInt(orderBy);
    }

    public static final Creator<Flag> CREATOR = new Creator<Flag>() {
        @Override
        public Flag createFromParcel(Parcel in) {
            return new Flag(in);
        }

        @Override
        public Flag[] newArray(int size) {
            return new Flag[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Flag item = (Flag) in;
        return Objects.equal(id, item.id)
                && Objects.equal(type, item.type)
                && Objects.equal(subtype, item.subtype);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, subtype);
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public boolean hasProperty(String type, String subtype) {
        return Objects.equal(type, this.type) && Objects.equal(subtype, this.subtype);
    }
}
*/
