package com.dreampany.lca.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Exchange extends Base {

    private String exchange;
    private String fromSymbol;
    private String toSymbol;
    private double volume24h;
    private double volume24hTo;

    @Ignore
    public Exchange() {

    }

    public Exchange(long id) {
        this.id = id;
    }

/*    public Exchange(@NonNull String exchange, @NonNull String fromSymbol, @NonNull String toSymbol) {
        this.exchange = exchange;
        this.fromSymbol = fromSymbol;
        this.toSymbol = toSymbol;
    }*/

    @Ignore
    private Exchange(Parcel in) {
        super(in);
        exchange = in.readString();
        fromSymbol = in.readString();
        toSymbol = in.readString();
        volume24h = in.readDouble();
        volume24hTo = in.readDouble();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeString(exchange);
        dest.writeString(fromSymbol);
        dest.writeString(toSymbol);
        dest.writeDouble(volume24h);
        dest.writeDouble(volume24hTo);
    }

    public static final Creator<Exchange> CREATOR = new Creator<Exchange>() {
        @Override
        public Exchange createFromParcel(Parcel in) {
            return new Exchange(in);
        }

        @Override
        public Exchange[] newArray(int size) {
            return new Exchange[size];
        }
    };

    @Override
    public String toString() {
        return "Exchange{" +
                "exchange=" + exchange +
                ", toSymbol='" + toSymbol + '\'' +
                ", fromSymbol='" + fromSymbol + '\'' +
                '}';
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setFromSymbol( String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public void setToSymbol( String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public void setVolume24h(double volume24h) {
        this.volume24h = volume24h;
    }

    public void setVolume24hTo(double volume24hTo) {
        this.volume24hTo = volume24hTo;
    }

    public String getExchange() {
        return exchange;
    }

    public String getFromSymbol() {
        return fromSymbol;
    }

    public String getToSymbol() {
        return toSymbol;
    }

    public double getVolume24h() {
        return volume24h;
    }

    public double getVolume24hTo() {
        return volume24hTo;
    }
}
