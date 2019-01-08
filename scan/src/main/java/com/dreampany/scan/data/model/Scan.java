package com.dreampany.scan.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.dreampany.scan.data.enums.ScanType;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Scan extends Base {

    private String text;
    private ScanType type;
    private long time;

    @Ignore
    public Scan() {

    }

    public Scan(String text, ScanType type) {
        this.text = text;
        this.type = type;
    }

    @Ignore
    private Scan(Parcel in) {
        super(in);
        text = in.readString();
        type = in.readParcelable(ScanType.class.getClassLoader());
        time = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(text);
        dest.writeParcelable(type, flags);
        dest.writeLong(time);
    }

    public static final Creator<Scan> CREATOR = new Creator<Scan>() {
        @Override
        public Scan createFromParcel(Parcel in) {
            return new Scan(in);
        }

        @Override
        public Scan[] newArray(int size) {
            return new Scan[size];
        }
    };

    public void setText(String text) {
        this.text = text;
    }

    public void setType(ScanType type) {
        this.type = type;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public ScanType getType() {
        return type;
    }

    @Override
    public long getTime() {
        return time;
    }
}
