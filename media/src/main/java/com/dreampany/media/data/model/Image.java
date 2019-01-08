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
public class Image extends Media {

    private long dateTaken;

    public Image() {
        super();
        setMediaType(MediaType.IMAGE);
    }

    private Image(Parcel in) {
        super(in);
        dateTaken = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(dateTaken);
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public long getDateTaken() {
        return dateTaken;
    }
}
