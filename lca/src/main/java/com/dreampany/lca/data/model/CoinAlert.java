/*
package com.dreampany.lca.data.model;

import android.os.Parcel;

import com.dreampany.frame.data.model.Alert;
import com.dreampany.lca.misc.Constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

*/
/**
 * Created by Roman-372 on 2/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

@Entity(indices = {@Index(value = {Constants.CoinAlert.ID}, unique = true)},
        primaryKeys = {Constants.CoinAlert.ID})
public class CoinAlert extends Alert {

    private double priceUp;
    private double priceDown;
    private double dayChange;
    private long periodicTime;

    public CoinAlert() {
    }

    @Ignore
    private CoinAlert(Parcel in) {
        super(in);
        priceUp = in.readDouble();
        priceDown = in.readDouble();
        dayChange = in.readDouble();
        periodicTime = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
*/
