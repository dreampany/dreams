package com.dreampany.media.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.media.data.enums.MediaType;

/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Apk extends Media {

    private String packageName;

    public Apk() {
        super();
        setMediaType(MediaType.APK);
    }

    private Apk(Parcel in) {
        super(in);
        packageName = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(packageName);
    }

    public static final Creator<Apk> CREATOR = new Creator<Apk>() {
        @Override
        public Apk createFromParcel(Parcel in) {
            return new Apk(in);
        }

        @Override
        public Apk[] newArray(int size) {
            return new Apk[size];
        }
    };

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}
