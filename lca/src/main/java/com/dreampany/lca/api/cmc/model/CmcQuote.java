package com.dreampany.lca.api.cmc.model;

import com.dreampany.framework.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CmcQuote implements Serializable {

    private final double price;
    private final double dayVolume;
    private final double marketCap;
    private final double hourChange;
    private final double dayChange;
    private final double weekChange;
    private final String lastUpdated;

    @JsonCreator
    public CmcQuote(@JsonProperty("price") double price,
                    @JsonProperty("volume_24h") double dayVolume,
                    @JsonProperty("market_cap") double marketCap,
                    @JsonProperty("percent_change_1h") double hourChange,
                    @JsonProperty("percent_change_24h") double dayChange,
                    @JsonProperty("percent_change_7d") double weekChange,
                    @JsonProperty("last_updated") String lastUpdated) {
        this.price = price;
        this.dayVolume = dayVolume;
        this.marketCap = marketCap;
        this.hourChange = hourChange;
        this.dayChange = dayChange;
        this.weekChange = weekChange;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmcQuote that = (CmcQuote) o;

        if (Double.compare(that.price, price) != 0) return false;
        if (Double.compare(that.dayVolume, dayVolume) != 0) return false;
        if (Double.compare(that.hourChange, hourChange) != 0) return false;
        if (Double.compare(that.dayChange, dayChange) != 0) return false;
        if (Double.compare(that.weekChange, weekChange) != 0) return false;
        return Double.compare(that.marketCap, marketCap) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(price);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dayVolume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(hourChange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dayChange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weekChange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(marketCap);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CmcQuote{" +
                "price=" + price +
                ", dayVolume=" + dayVolume +
                ", marketCap=" + marketCap +
                ", hourChange=" + hourChange +
                ", dayChange=" + dayChange +
                ", weekChange=" + weekChange +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    public double getPrice() {
        return price;
    }

    public double getDayVolume() {
        return dayVolume;
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

    public double getMarketCap() {
        return marketCap;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public long getLastUpdatedTime() {
        return TimeUtil.getUtcTime(getLastUpdated());
    }
}
