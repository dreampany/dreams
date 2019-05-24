package com.dreampany.lca.data.model;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.dreampany.frame.data.model.Base;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.misc.Constants;
import com.google.common.collect.Maps;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Entity(indices = {@Index(value = {Constants.Coin.ID}, unique = true)},
        primaryKeys = {Constants.Coin.ID})
@IgnoreExtraProperties
public class Coin extends Base {

    private CoinSource source;
/*    @PropertyName(Constants.Coin.COIN_ID)
    private long coinId;*/
    private String name;
    private String symbol;
    private String slug;
    private int rank;
    @PropertyName(Constants.Coin.MARKET_PAIRS)
    private int marketPairs;
    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    private double circulatingSupply;
    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    private double totalSupply;
    @PropertyName(Constants.Coin.MAX_SUPPLY)
    private double maxSupply;
    @PropertyName(Constants.Coin.LAST_UPDATED)
    private long lastUpdated;
    @PropertyName(Constants.Coin.DATE_ADDED)
    private long dateAdded;
    private List<String> tags;
    @Ignore
    @Exclude
    private Map<Currency, Quote> quotes;

    @Ignore
    public Coin() {

    }

    public Coin(long id) {
        this.id = id;
    }

    @Ignore
    private Coin(Parcel in) {
        super(in);
        source = in.readParcelable(CoinSource.class.getClassLoader());
        //coinId = in.readLong();
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
        quotes = (Map<Currency, Quote>) in.readSerializable();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(source, flags);
        //dest.writeLong(coinId);
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
        dest.writeSerializable((Serializable) quotes);
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

    public void setSource(CoinSource source) {
        this.source = source;
    }

    public CoinSource getSource() {
        return source;
    }

    @Override
    public void setId(long id) {
        super.setId(id);
    }

    @Override
    public long getId() {
        return super.getId();
    }

/*    @PropertyName(Constants.Coin.COIN_ID)
    public void setCoinId(long coinId) {
        this.coinId = coinId;
    }

    @PropertyName(Constants.Coin.COIN_ID)
    public long getCoinId() {
        return coinId;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    @PropertyName(Constants.Coin.MARKET_PAIRS)
    public void setMarketPairs(int marketPairs) {
        this.marketPairs = marketPairs;
    }

    @PropertyName(Constants.Coin.MARKET_PAIRS)
    public int getMarketPairs() {
        return marketPairs;
    }

    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    public double getTotalSupply() {
        return totalSupply;
    }

    @PropertyName(Constants.Coin.MAX_SUPPLY)
    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    @PropertyName(Constants.Coin.MAX_SUPPLY)
    public double getMaxSupply() {
        return maxSupply;
    }

    @PropertyName(Constants.Coin.LAST_UPDATED)
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @PropertyName(Constants.Coin.LAST_UPDATED)
    public long getLastUpdated() {
        return lastUpdated;
    }

    @Exclude
    public Date getLastUpdatedDate() {
        return new Date(getLastUpdated());
    }

    @PropertyName(Constants.Coin.DATE_ADDED)
    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    @PropertyName(Constants.Coin.DATE_ADDED)
    public long getDateAdded() {
        return dateAdded;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setQuotes(Map<Currency, Quote> quotes) {
        this.quotes = quotes;
    }

    public void addQuote(Quote quote) {
        if (quotes == null) {
            quotes = Maps.newHashMap();
        }
        quotes.put(quote.getCurrency(), quote);
    }

    @Exclude
    public Map<Currency, Quote> getQuotes() {
        return quotes;
    }

    @Exclude
    public List<Quote> getQuotesAsList() {
        if (quotes == null) {
            return null;
        }
        return new ArrayList<>(quotes.values());
    }

    public void clearQuote() {
        quotes.clear();
    }

    public boolean hasQuote() {
        if (quotes == null) {
            return false;
        }
        return !quotes.isEmpty();
    }

    public boolean hasQuote(String currency) {
        if (quotes == null) {
            return false;
        }
        return quotes.containsKey(Currency.valueOf(currency));
    }

    public boolean hasQuote(Currency currency) {
        if (quotes == null) {
            return false;
        }
        return quotes.containsKey(currency);
    }

    public boolean hasQuote(Currency[] currencies) {
        if (quotes == null) {
            return false;
        }
        for (Currency currency : currencies) {
            if (!quotes.containsKey(currency)) {
                return false;
            }
        }
        return true;
    }

    public void addQuotes(List<Quote> quotes) {
        for (Quote quote : quotes) {
            addQuote(quote);
        }
    }

    public Quote getQuote(Currency currency) {
        if (quotes != null) {
            return quotes.get(currency);
        }
        return null;
    }
}
