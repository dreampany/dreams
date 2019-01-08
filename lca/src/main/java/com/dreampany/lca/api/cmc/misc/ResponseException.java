package com.dreampany.lca.api.cmc.misc;

import com.dreampany.lca.api.cmc.model.CmcResponse;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class ResponseException extends RuntimeException {

    private final CmcResponse<?> cmcResponse;

    public ResponseException(CmcResponse<?> apiCmcResponse) {
        super("Failed API Request. Returned: " + apiCmcResponse.getErrorMessage());
        this.cmcResponse = apiCmcResponse;
    }

    public CmcResponse<?> getApiResponse() {
        return cmcResponse;
    }
}
