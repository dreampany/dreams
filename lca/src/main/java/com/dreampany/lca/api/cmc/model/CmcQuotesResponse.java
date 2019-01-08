package com.dreampany.lca.api.cmc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by Hawladar Roman on 1/4/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CmcQuotesResponse extends CmcResponse<Map<String, CmcCoin>> {

    @JsonCreator
    public CmcQuotesResponse(@JsonProperty(KEY_STATUS) Status status,
                             @JsonProperty(KEY_DATA) Map<String, CmcCoin> data) {
        super(status, data);
    }

    public long count() {
        return data.size();
    }
}
