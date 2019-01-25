package com.dreampany.radio.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;
import com.dreampany.frame.data.model.Base;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by Hawladar Roman on 1/8/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Entity(indices = {@Index(value = {"uri"}, unique = true)}, primaryKeys = {"uri"})
public class Station extends Base {

    @NonNull
    private String uri;
    private String name;
    private String mimeType;
    private int sampleRate;
    private int bitrate;

    @Ignore
    public Station() {

    }

    public Station(@NonNull String uri) {
        this.uri = uri;
    }

    @Ignore
    private Station(Parcel in) {
        super(in);
        uri = in.readString();
        name = in.readString();
        mimeType = in.readString();
        sampleRate = in.readInt();
        bitrate = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(uri);
        dest.writeString(name);
        dest.writeString(mimeType);
        dest.writeInt(sampleRate);
        dest.writeInt(bitrate);
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Station item = (Station) in;
        return Objects.equal(uri, item.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uri);
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void setUri(@NonNull String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    @NonNull
    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getBitrate() {
        return bitrate;
    }
}
