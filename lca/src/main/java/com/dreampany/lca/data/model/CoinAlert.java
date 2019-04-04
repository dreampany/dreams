package com.dreampany.lca.data.model;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import com.dreampany.frame.data.model.Alert;
import com.google.common.base.Objects;

/**
 * Created by Roman-372 on 2/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class CoinAlert extends Alert {

    private long coinId;
    private double priceUp;
    private double priceDown;
    private double dayChange;
    private long periodicTime;

    public CoinAlert() {
    }

    @Ignore
    private CoinAlert(Parcel in) {
        super(in);
        coinId = in.readLong();
        priceUp = in.readDouble();
        priceDown = in.readDouble();
        dayChange = in.readDouble();
        periodicTime = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(coinId);
        dest.writeDouble(priceUp);
        dest.writeDouble(priceDown);
        dest.writeDouble(dayChange);
        dest.writeLong(periodicTime);
    }

    public static final Creator<CoinAlert> CREATOR = new Creator<CoinAlert>() {
        @Override
        public CoinAlert createFromParcel(Parcel in) {
            return new CoinAlert(in);
        }

        @Override
        public CoinAlert[] newArray(int size) {
            return new CoinAlert[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        CoinAlert item = (CoinAlert) in;
        return coinId == item.coinId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coinId);
    }

    public void setCoinId(long coinId) {
        this.coinId = coinId;
    }

    public void setPriceUp(double priceUp) {
        this.priceUp = priceUp;
    }

    public void setPriceDown(double priceDown) {
        this.priceDown = priceDown;
    }

    public void setDayChange(double dayChange) {
        this.dayChange = dayChange;
    }

    public void setPeriodicTime(long periodicTime) {
        this.periodicTime = periodicTime;
    }

    public long getCoinId() {
        return coinId;
    }

    public double getPriceUp() {
        return priceUp;
    }

    public double getPriceDown() {
        return priceDown;
    }

    public double getDayChange() {
        return dayChange;
    }

    public long getPeriodicTime() {
        return periodicTime;
    }

    public boolean hasPriceUp() {
        return priceUp != 0.0f;
    }

    public boolean hasPriceDown() {
        return priceDown != 0.0f;
    }

}
