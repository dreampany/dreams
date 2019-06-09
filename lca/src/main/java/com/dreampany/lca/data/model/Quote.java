package com.dreampany.lca.data.model;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.dreampany.frame.data.model.Base;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.misc.Constants;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by Roman on 1/14/2019
 * Copyright (c) 2019 Dreampany. All rights reserved.
 * dreampanymail@gmail.com
 * Last modified $file.lastModified
 */

@Entity(indices = {@Index(value = {Constants.Quote.ID, Constants.Quote.CURRENCY}, unique = true)},
        primaryKeys = {Constants.Quote.ID, Constants.Quote.CURRENCY})
@IgnoreExtraProperties
public class Quote extends Base {

    @NonNull
    private Currency currency;
    private double price;
    @PropertyName(Constants.Quote.DAY_VOLUME)
    private double dayVolume;
    @PropertyName(Constants.Quote.MARKET_CAP)
    private double marketCap;
    @PropertyName(Constants.Quote.HOUR_CHANGE)
    private double hourChange;
    @PropertyName(Constants.Quote.DAY_CHANGE)
    private double dayChange;
    @PropertyName(Constants.Quote.WEEK_CHANGE)
    private double weekChange;
    @PropertyName(Constants.Quote.LAST_UPDATED)
    private long lastUpdated;

    @Ignore
    public Quote() {

    }

    public Quote(String id) {
        this.id = id;
    }

    @Ignore
    private Quote(Parcel in) {
        super(in);
        currency = in.readParcelable(Currency.class.getClassLoader());
        price = in.readDouble();
        dayVolume = in.readDouble();
        marketCap = in.readDouble();
        hourChange = in.readDouble();
        dayChange = in.readDouble();
        weekChange = in.readDouble();
        lastUpdated = in.readLong();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(currency, flags);
        dest.writeDouble(price);
        dest.writeDouble(dayVolume);
        dest.writeDouble(marketCap);
        dest.writeDouble(hourChange);
        dest.writeDouble(dayChange);
        dest.writeDouble(weekChange);
        dest.writeLong(lastUpdated);
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

    public void setCurrency(@NonNull Currency currency) {
        this.currency = currency;
    }

    @NonNull
    public Currency getCurrency() {
        return currency;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @PropertyName(Constants.Quote.DAY_VOLUME)
    public void setDayVolume(double dayVolume) {
        this.dayVolume = dayVolume;
    }

    @PropertyName(Constants.Quote.DAY_VOLUME)
    public double getDayVolume() {
        return dayVolume;
    }

    @PropertyName(Constants.Quote.MARKET_CAP)
    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    @PropertyName(Constants.Quote.MARKET_CAP)
    public double getMarketCap() {
        return marketCap;
    }

    @PropertyName(Constants.Quote.HOUR_CHANGE)
    public void setHourChange(double hourChange) {
        this.hourChange = hourChange;
    }

    @PropertyName(Constants.Quote.HOUR_CHANGE)
    public double getHourChange() {
        return hourChange;
    }

    @PropertyName(Constants.Quote.DAY_CHANGE)
    public void setDayChange(double dayChange) {
        this.dayChange = dayChange;
    }

    @PropertyName(Constants.Quote.DAY_CHANGE)
    public double getDayChange() {
        return dayChange;
    }

    @PropertyName(Constants.Quote.WEEK_CHANGE)
    public void setWeekChange(double weekChange) {
        this.weekChange = weekChange;
    }

    @PropertyName(Constants.Quote.WEEK_CHANGE)
    public double getWeekChange() {
        return weekChange;
    }

    @PropertyName(Constants.Quote.LAST_UPDATED)
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @PropertyName(Constants.Quote.LAST_UPDATED)
    public long getLastUpdated() {
        return lastUpdated;
    }
}
