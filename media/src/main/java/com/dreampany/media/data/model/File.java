package com.dreampany.media.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dreampany.media.data.enums.MediaType;

/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class File extends Media {

    public File() {
        super();
        setMediaType(MediaType.FILE);
    }

    private File(Parcel in) {
        super(in);

    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

    }

    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {
        @Override
        public File createFromParcel(Parcel in) {
            return new File(in);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };
}
