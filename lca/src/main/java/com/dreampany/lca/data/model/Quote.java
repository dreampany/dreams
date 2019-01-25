package com.dreampany.lca.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;
import com.dreampany.frame.data.model.Base;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by Roman on 1/14/2019
 * Copyright (c) 2019 Dreampany. All rights reserved.
 * dreampanymail@gmail.com
 * Last modified $file.lastModified
 */

@Entity(indices = {@Index(value = {"coinId", "currency"}, unique = true)},
        primaryKeys = {"coinId", "currency"})
public class Quote extends Base {

    private long coinId; // id of coin
    @NonNull
    private Currency currency;
    private double price;
    private double dayVolume;
    private double marketCap;
    private double hourChange;
    private double dayChange;
    private double weekChange;
    private String lastUpdated;

    @Ignore
    public Quote() {

    }

    public Quote(long id) {
        this.id = id;
    }

    @Ignore
    private Quote(Parcel in) {
        super(in);
        coinId = in.readLong();
        currency = in.readParcelable(Currency.class.getClassLoader());
        price = in.readDouble();
        dayVolume = in.readDouble();
        marketCap = in.readDouble();
        hourChange = in.readDouble();
        dayChange = in.readDouble();
        weekChange = in.readDouble();
        lastUpdated = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(coinId);
        dest.writeParcelable(currency, flags);
        dest.writeDouble(price);
        dest.writeDouble(dayVolume);
        dest.writeDouble(marketCap);
        dest.writeDouble(hourChange);
        dest.writeDouble(dayChange);
        dest.writeDouble(weekChange);
        dest.writeString(lastUpdated);
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void setCoinId(long coinId) {
        this.coinId = coinId;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDayVolume(double dayVolume) {
        this.dayVolume = dayVolume;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public void setHourChange(double hourChange) {
        this.hourChange = hourChange;
    }

    public void setDayChange(double dayChange) {
        this.dayChange = dayChange;
    }

    public void setWeekChange(double weekChange) {
        this.weekChange = weekChange;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getCoinId() {
        return coinId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getPrice() {
        return price;
    }

    public double getDayVolume() {
        return dayVolume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getHourChange() {
        return hourChange;
    }

    public double getDayChange() {
        return dayChange;
    }

    public double getWeekChange() {
        return weekChange;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
