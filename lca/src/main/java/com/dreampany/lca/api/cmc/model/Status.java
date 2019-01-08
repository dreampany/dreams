package com.dreampany.lca.api.cmc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hawladar Roman on 1/2/2019.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Status {
    /*        "timestamp": "2019-01-02T04:53:00.527Z",
                    "error_code": 0,
                    "error_message": null,
                    "elapsed": 9,
                    "credit_count": 1*/
    private final String timestamp;
    private final int errorCode;
    private final String errorMessage;
    private final int elapsed;
    private final int creditCount;

    @JsonCreator
    public Status(@JsonProperty("timestamp") String timestamp,
                  @JsonProperty("error_code") int errorCode,
                  @JsonProperty("error_message") String errorMessage,
                  @JsonProperty("elapsed") int elapsed,
                  @JsonProperty("credit_count") int creditCount) {
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.elapsed = elapsed;
        this.creditCount = creditCount;
    }

    public boolean hasError() {
        return errorCode != 0;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
