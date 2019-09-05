package com.dreampany.lca.api.cmc.model;

import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.misc.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmcCoin {

    private final long id;
    private final String name;
    private final String symbol;
    private final String slug;
    private final int rank;
    private final int marketPairs;
    private final double circulatingSupply;
    private final double totalSupply;
    private final double maxSupply;
    private final String lastUpdated;
    private final String dateAdded;
    private final List<String> tags;
    private final Map<CmcCurrency, CmcQuote> priceQuote;

    @JsonCreator
    public CmcCoin(@JsonProperty(Constants.CmcCoinKey.ID) long id,
                   @JsonProperty(Constants.CmcCoinKey.NAME) String name,
                   @JsonProperty(Constants.CmcCoinKey.SYMBOL) String symbol,
                   @JsonProperty(Constants.CmcCoinKey.SLUG) String slug,
                   @JsonProperty(Constants.CmcCoinKey.RANK) int rank,
                   @JsonProperty(Constants.CmcCoinKey.MARKET_PAIRS) int marketPairs,
                   @JsonProperty(Constants.CmcCoinKey.CIRCULATING_SUPPLY) double circulatingSupply,
                   @JsonProperty(Constants.CmcCoinKey.TOTAL_SUPPLY) double totalSupply,
                   @JsonProperty(Constants.CmcCoinKey.MAX_SUPPLY) double maxSupply,
                   @JsonProperty(Constants.CmcCoinKey.LAST_UPDATED) String lastUpdated,
                   @JsonProperty(Constants.CmcCoinKey.DATE_ADDED) String dateAdded,
                   @JsonProperty(Constants.CmcCoinKey.TAGS) List<String> tags,
                   @JsonProperty(Constants.CmcCoinKey.QUOTE) Map<CmcCurrency, CmcQuote> priceQuote) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.rank = rank;
        this.marketPairs = marketPairs;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.maxSupply = maxSupply;
        this.lastUpdated = lastUpdated;
        this.dateAdded = dateAdded;
        this.tags = tags;
        this.priceQuote = priceQuote;
    }

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        CmcCoin item = (CmcCoin) in;
        return Objects.equal(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public long getId() {
        return id;
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

    public String getLastUpdated() {
        return lastUpdated;
    }

    public long getLastUpdatedTime() {
        return TimeUtil.getUtcTime(getLastUpdated());
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public long getDateAddedTime() {
        return TimeUtil.getUtcTime(getDateAdded());
    }

    public List<String> getTags() {
        return tags;
    }


    public Map<CmcCurrency, CmcQuote> getPriceQuote() {
        return priceQuote;
    }

    public CmcQuote getPriceQuote(CmcCurrency cmcCurrency) {
        return getPriceQuote().get(cmcCurrency);
    }

    public CmcQuote getUSDPriceQuote() {
        return getPriceQuote(CmcCurrency.USD);
    }

    public CmcQuote getBTCPriceQuote() {
        return getPriceQuote(CmcCurrency.BTC);
    }

    /*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmcCoin coin = (CmcCoin) o;

        if (id != coin.id) return false;
        if (rank != coin.rank) return false;
        if (Double.compare(coin.circulatingSupply, circulatingSupply) != 0) return false;
        if (Double.compare(coin.totalSupply, totalSupply) != 0) return false;
        if (Double.compare(coin.maxSupply, maxSupply) != 0) return false;
        if (lastUpdated != coin.lastUpdated) return false;
        if (name != null ? !name.equals(coin.name) : coin.name != null) return false;
        if (symbol != null ? !symbol.equals(coin.symbol) : coin.symbol != null) return false;
        if (websiteSlug != null ? !websiteSlug.equals(coin.websiteSlug) : coin.websiteSlug != null)
            return false;
        return priceQuotes != null ? priceQuotes.equals(coin.priceQuotes) : coin.priceQuotes == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + (websiteSlug != null ? websiteSlug.hashCode() : 0);
        result = 31 * result + rank;
        temp = Double.doubleToLongBits(circulatingSupply);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalSupply);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxSupply);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (priceQuotes != null ? priceQuotes.hashCode() : 0);
        result = 31 * result + (int) (lastUpdated ^ (lastUpdated >>> 32));
        return result;
    }*/

    @Override
    public String toString() {
        return "CoinMarket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", slug='" + slug + '\'' +
                ", rank=" + rank +
                ", circulatingSupply=" + circulatingSupply +
                ", totalSupply=" + totalSupply +
                ", maxSupply=" + maxSupply +
                ", priceQuote=" + priceQuote +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
