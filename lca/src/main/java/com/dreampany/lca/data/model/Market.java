package com.dreampany.lca.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import android.os.Parcel;
import androidx.annotation.NonNull;

import com.dreampany.frame.data.model.Base;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Market extends Base {

    private String market;
    private String fromSymbol;
    private String toSymbol;
    private double price;
    private double volume24h;
    private double changePct24h;
    private double change24h;

    @Ignore
    public Market() {

    }

    public Market(long id) {
        this.id = id;
    }

/*    public Market(@NonNull String market, @NonNull String fromSymbol, @NonNull String toSymbol) {
        this.market = market;
        this.fromSymbol = fromSymbol;
        this.market = toSymbol;
    }*/

    @Ignore
    private Market(Parcel in) {
        super(in);
        market = in.readString();
        fromSymbol = in.readString();
        toSymbol = in.readString();
        price = in.readDouble();
        volume24h = in.readDouble();
        changePct24h = in.readDouble();
        change24h = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(market);
        dest.writeString(fromSymbol);
        dest.writeString(toSymbol);
        dest.writeDouble(price);
        dest.writeDouble(volume24h);
        dest.writeDouble(changePct24h);
        dest.writeDouble(change24h);
    }

    public static final Creator<Market> CREATOR = new Creator<Market>() {
        @Override
        public Market createFromParcel(Parcel in) {
            return new Market(in);
        }

        @Override
        public Market[] newArray(int size) {
            return new Market[size];
        }
    };

    @Override
    public String toString() {
        return "Market{" +
                "market=" + market +
                ", toSymbol='" + toSymbol + '\'' +
                ", fromSymbol='" + fromSymbol + '\'' +
                '}';
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public void setFromSymbol( String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public void setToSymbol( String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume24h(double volume24h) {
        this.volume24h = volume24h;
    }

    public void setChangePct24h(double changePct24h) {
        this.changePct24h = changePct24h;
    }

    public void setChange24h(double change24h) {
        this.change24h = change24h;
    }

    public String getMarket() {
        return market;
    }

    public String getFromSymbol() {
        return fromSymbol;
    }

    public String getToSymbol() {
        return toSymbol;
    }

    public double getPrice() {
        return price;
    }

    public double getVolume24h() {
        return volume24h;
    }

    public double getChangePct24h() {
        return changePct24h;
    }

    public double getChange24h() {
        return change24h;
    }
}
