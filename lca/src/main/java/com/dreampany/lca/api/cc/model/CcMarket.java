package com.dreampany.lca.api.cc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcMarket {

    private final String market;
    private final String fromSymbol;
    private final String toSymbol;
    private final double price;
    private final double volume24h;
    private final double changePCT24h;
    private final double change24h;

    @JsonCreator
    public CcMarket(@JsonProperty("MARKET") String market,
                    @JsonProperty("FROMSYMBOL") String fromSymbol,
                    @JsonProperty("TOSYMBOL")  String toSymbol,
                    @JsonProperty("PRICE") double price,
                    @JsonProperty("VOLUME24HOUR") double volume24h,
                    @JsonProperty("CHANGEPCT24HOUR") double changePCT24h,
                    @JsonProperty("CHANGE24HOUR") double change24h) {
        this.market = market;
        this.fromSymbol = fromSymbol;
        this.toSymbol = toSymbol;
        this.price = price;
        this.volume24h = volume24h;
        this.changePCT24h = changePCT24h;
        this.change24h = change24h;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(market, fromSymbol, toSymbol);
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

    public double getChangePCT24h() {
        return changePCT24h;
    }

    public double getChange24h() {
        return change24h;
    }
}
