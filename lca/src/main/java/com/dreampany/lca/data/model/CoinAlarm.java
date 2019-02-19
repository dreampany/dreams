package com.dreampany.lca.data.model;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import com.dreampany.frame.data.model.Alarm;
import com.google.common.base.Objects;

/**
 * Created by Roman-372 on 2/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class CoinAlarm extends Alarm {

    private String symbol;
    private double dayChange;
    private long periodicTime;

    public CoinAlarm() {
    }

    @Ignore
    private CoinAlarm(Parcel in) {
        super(in);
        symbol = in.readString();
        dayChange = in.readDouble();
        periodicTime = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(symbol);
        dest.writeDouble(dayChange);
        dest.writeLong(periodicTime);
    }

    public static final Creator<CoinAlarm> CREATOR = new Creator<CoinAlarm>() {
        @Override
        public CoinAlarm createFromParcel(Parcel in) {
            return new CoinAlarm(in);
        }

        @Override
        public CoinAlarm[] newArray(int size) {
            return new CoinAlarm[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        CoinAlarm item = (CoinAlarm) in;
        return Objects.equal(symbol, item.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDayChange(double dayChange) {
        this.dayChange = dayChange;
    }

    public void setPeriodicTime(long periodicTime) {
        this.periodicTime = periodicTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getDayChange() {
        return dayChange;
    }

    public long getPeriodicTime() {
        return periodicTime;
    }
}
