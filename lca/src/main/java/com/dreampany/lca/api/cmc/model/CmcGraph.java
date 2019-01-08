package com.dreampany.lca.api.cmc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by Hawladar Roman on 12/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class CmcGraph {

    private String websiteSlug;
    private long startTime;
    private long endTime;
    private final List<List<Float>> priceBtc;
    private final List<List<Float>> priceUsd;
    private final List<List<Float>> volumeUsd;

    @JsonCreator
    public CmcGraph(@JsonProperty("price_btc") List<List<Float>> priceBtc,
                    @JsonProperty("price_usd") List<List<Float>> priceUsd,
                    @JsonProperty("volume_usd") List<List<Float>> volumeUsd) {
        this.priceUsd = priceUsd;
        this.priceBtc = priceBtc;
        this.volumeUsd = volumeUsd;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(websiteSlug);
    }

    public void setWebsiteSlug(String websiteSlug) {
        this.websiteSlug = websiteSlug;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getWebsiteSlug() {
        return websiteSlug;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<List<Float>> getPriceBtc() {
        return priceBtc;
    }

    public List<List<Float>> getPriceUsd() {
        return priceUsd;
    }

    public List<List<Float>> getVolumeUsd() {
        return volumeUsd;
    }
}
