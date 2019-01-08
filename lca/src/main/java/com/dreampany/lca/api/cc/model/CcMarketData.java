package com.dreampany.lca.api.cc.model;

import com.dreampany.lca.api.misc.Response;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcMarketData extends Response<List<CcMarket>> {

    private static final String JSON_EXCHANGES_PROPERTY = "Exchanges";

    @JsonCreator
    public CcMarketData(@JsonProperty(JSON_EXCHANGES_PROPERTY) List<CcMarket> data) {
        super(data);
    }
}

