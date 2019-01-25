package com.dreampany.lca.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.BaseParcel;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 23/8/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class SourceInfo extends BaseParcel {

    @ColumnInfo(name = "sourceInfoId")
    private long id;
    @ColumnInfo(name = "sourceInfoName")
    private String name;
    @ColumnInfo(name = "sourceInfoLanguage")
    private String language;
    @ColumnInfo(name = "sourceInfoImageUrl")
    private String imageUrl;

    @Ignore
    public SourceInfo() {

    }

    public SourceInfo(long id) {
        this.id = id;
    }

    @Ignore
    private SourceInfo(Parcel in) {
        super(in);
        id = in.readLong();
        name = in.readString();
        language = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(language);
        dest.writeString(imageUrl);

    }

    public static final Parcelable.Creator<SourceInfo> CREATOR = new Parcelable.Creator<SourceInfo>() {
        @Override
        public SourceInfo createFromParcel(Parcel in) {
            return new SourceInfo(in);
        }

        @Override
        public SourceInfo[] newArray(int size) {
            return new SourceInfo[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        SourceInfo item = (SourceInfo) in;
        return Objects.equal(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
