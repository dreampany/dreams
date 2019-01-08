package com.dreampany.lca.api.cc.model;

import com.dreampany.lca.api.misc.Response;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by Hawladar Roman on 24/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcMarketResponse extends Response<CcMarketData> {
    private static final String KEY_RESPONSE = "Response";
    private static final String KEY_DATA = "Data";
    private static final String KEY_MESSAGE = "Message";
    private static final String VALUE_SUCCESS = "Success";
    private static final String VALUE_ERROR = "Error";

    private final String response;
    private final String message;

    @JsonCreator
    protected CcMarketResponse(@JsonProperty(KEY_RESPONSE) String response,
                               @JsonProperty(KEY_MESSAGE) String message,
                               @JsonProperty(KEY_DATA) CcMarketData data) {
        super(data);
        this.response = response;
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public List<CcMarket> getMarkets() {
        return data.getData();
    }

    public boolean isSuccess() {
        return Objects.equal(response, VALUE_SUCCESS);
    }
}
