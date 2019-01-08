package com.dreampany.lca.api.cc.model;

import com.dreampany.lca.api.misc.Response;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcExchangeResponse extends Response<List<CcExchange>> {

    private static final String KEY_RESPONSE = "Response";
    private static final String KEY_DATA = "Data";
    private static final String VALUE_SUCCESS = "Success";
    private static final String VALUE_ERROR = "Error";

    private final String response;

    @JsonCreator
    protected CcExchangeResponse(@JsonProperty(KEY_RESPONSE) String response,
                                 @JsonProperty(KEY_DATA) List<CcExchange> data) {
        super(data);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return Objects.equal(response, VALUE_SUCCESS);
    }
}
