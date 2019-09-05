package com.dreampany.lca.api.cmc.model;

import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.api.misc.Response;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public abstract class CmcResponse<T> extends Response<T> {

    public static final String KEY_STATUS = "status";
    public static final String KEY_DATA = "data";

    private final Status status;

    protected CmcResponse(Status status, T data) {
        super(data);
        this.status = status;
    }

    public boolean hasError() {
        return status.hasError();
    }

    public long getTimestamp() {
        return TimeUtil.getUtcTime(status.getTimestamp());
    }

    public String getErrorMessage() {
        return status.getErrorMessage();
    }

/*    public Map<String, Object> getStatus() {
        return status;
    }

    public boolean hasError() {
        return status.containsKey(KEY_ERROR_CODE) && status.get(KEY_ERROR_CODE) != null;
    }

    public Integer getErrorCode() {
        return (Integer) status.get(KEY_ERROR_CODE);
    }

    public String getErrorMessage() {
        return (String) status.get(KEY_ERROR_MESSAGE);
    }

    public long getTimestamp() {
        return TimeUtil.getUtcTime((String) status.get(KEY_TIMESTAMP));
    }*/

}
