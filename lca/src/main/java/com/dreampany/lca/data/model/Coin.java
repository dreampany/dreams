package com.dreampany.lca.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.frame.data.model.Base;
import com.dreampany.lca.api.cmc.enums.Currency;
import com.dreampany.lca.api.cmc.model.PriceQuote;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        primaryKeys = {"id"})
public class Coin extends Base {

    private long coinId;
    private String name;
    private String symbol;
    private String slug;
    private int rank;
    private int marketPairs;
    private double circulatingSupply;
    private double totalSupply;
    private double maxSupply;
    private long lastUpdated;
    private long dateAdded;
    private List<String> tags;
    private Map<Currency, PriceQuote> priceQuote;

    @Ignore
    public Coin() {

    }

    public Coin(long id) {
        this.id = id;
    }

/*    public Coin(long coinId, String name, @NonNull String symbol) {
        this.coinId = coinId;
        this.name = name;
        this.symbol = symbol;
    }*/

    @Ignore
    private Coin(Parcel in) {
        super(in);
        coinId = in.readLong();
        name = in.readString();
        symbol = in.readString();
        slug = in.readString();
        rank = in.readInt();
        marketPairs = in.readInt();
        circulatingSupply = in.readDouble();
        totalSupply = in.readDouble();
        maxSupply = in.readDouble();
        lastUpdated = in.readLong();
        dateAdded = in.readLong();
        tags = in.createStringArrayList();
        priceQuote = (Map<Currency, PriceQuote>) in.readSerializable();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(coinId);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(slug);
        dest.writeInt(rank);
        dest.writeInt(marketPairs);
        dest.writeDouble(circulatingSupply);
        dest.writeDouble(totalSupply);
        dest.writeDouble(maxSupply);
        dest.writeLong(lastUpdated);
        dest.writeLong(dateAdded);
        dest.writeStringList(tags);
        dest.writeSerializable((Serializable) priceQuote);
    }

    public static final Creator<Coin> CREATOR = new Creator<Coin>() {
        @Override
        public Coin createFromParcel(Parcel in) {
            return new Coin(in);
        }

        @Override
        public Coin[] newArray(int size) {
            return new Coin[size];
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setMarketPairs(int marketPairs) {
        this.marketPairs = marketPairs;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setPriceQuote(Map<Currency, PriceQuote> priceQuote) {
        this.priceQuote = priceQuote;
    }

    public long getCoinId() {
        return coinId;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSlug() {
        return slug;
    }

    public int getRank() {
        return rank;
    }

    public int getMarketPairs() {
        return marketPairs;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public Date getLastUpdatedDate() {
        return new Date(getLastUpdated());
    }

    public List<String> getTags() {
        return tags;
    }

    public Map<Currency, PriceQuote> getPriceQuote() {
        return priceQuote;
    }

    public PriceQuote getPriceQuote(Currency currency) {
        if (priceQuote != null) {
            return priceQuote.get(currency);
        }
        return null;
    }

    public PriceQuote getUsdPriceQuote() {
        return getPriceQuote(Currency.USD);
    }

    public PriceQuote getBtcPriceQuote() {
        return getPriceQuote(Currency.BTC);
    }
}
