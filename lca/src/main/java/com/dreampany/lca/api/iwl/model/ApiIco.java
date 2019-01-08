package com.dreampany.lca.api.iwl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ApiIco {

    private final String name;
    private final String imageUrl;
    private final String description;
    private final String websiteLink;
    private final String icoWatchListUrl;
    private final String startTime;
    private final String endTime;
    private final String timezone;
    private final String coinSymbol;
    private final String priceUSD;
    private final String allTimeRoi;

    @JsonCreator
    public ApiIco(@JsonProperty("name") String name,
                  @JsonProperty("image") String imageUrl,
                  @JsonProperty("description") String description,
                  @JsonProperty("website_link") String websiteLink,
                  @JsonProperty("icowatchlist_url") String icoWatchListUrl,
                  @JsonProperty("start_time") String startTime,
                  @JsonProperty("end_time") String endTime,
                  @JsonProperty("timezone") String timezone,
                  @JsonProperty("coin_symbol") String coinSymbol,
                  @JsonProperty("price_usd") String priceUSD,
                  @JsonProperty("all_time_roi") String allTimeRoi
                  ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.websiteLink = websiteLink;
        this.icoWatchListUrl = icoWatchListUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timezone = timezone;
        this.coinSymbol = coinSymbol;
        this.priceUSD = priceUSD;
        this.allTimeRoi = allTimeRoi;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public String getIcoWatchListUrl() {
        return icoWatchListUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public String getPriceUSD() {
        return priceUSD;
    }

    public String getAllTimeRoi() {
        return allTimeRoi;
    }
}
