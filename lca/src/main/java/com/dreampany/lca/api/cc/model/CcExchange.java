package com.dreampany.lca.api.cc.model;

import com.dreampany.framework.util.DataUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CcExchange {

    private final String exchange;
    private final String fromSymbol;
    private final String toSymbol;
    private final double volume24h;
    private final double volume24hTo;

    @JsonCreator
    public CcExchange(@JsonProperty("exchange") String exchange,
                      @JsonProperty("fromsymbol") String fromSymbol,
                      @JsonProperty("tosymbol")  String toSymbol,
                      @JsonProperty("volume24h") double volume24h,
                      @JsonProperty("volume24hTo") double volume24hTo) {
        this.exchange = exchange;
        this.fromSymbol = fromSymbol;
        this.toSymbol = toSymbol;
        this.volume24h = volume24h;
        this.volume24hTo = volume24hTo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exchange, fromSymbol, toSymbol);
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
