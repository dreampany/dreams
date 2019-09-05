package com.dreampany.lca.api.cmc.model;

import com.dreampany.framework.util.DataUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CmcCoinResponse extends CmcResponse<List<CmcCoin>> {

    @JsonCreator
    public CmcCoinResponse(@JsonProperty(KEY_STATUS) Status status,
                           @JsonProperty(KEY_DATA) List<CmcCoin> data) {
        super(status, data);
    }

    public CmcCoin getItem() {
        if (!DataUtil.isEmpty(data)) {
            return data.get(0);
        }
        return null;
    }
}
