package com.dreampany.lca.api.cmc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CmcListingResponse extends CmcResponse<List<CmcCoin>> {

    @JsonCreator
    public CmcListingResponse(@JsonProperty(KEY_STATUS) Status status,
                              @JsonProperty(KEY_DATA) List<CmcCoin> data) {
        super(status, data);
    }

    public long count() {
        return data.size();
    }
}
