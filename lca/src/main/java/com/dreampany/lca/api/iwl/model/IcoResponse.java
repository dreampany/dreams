package com.dreampany.lca.api.iwl.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class IcoResponse extends Response<Map<String, List<ApiIco>>> {
    public IcoResponse(@JsonProperty(JSON_ICO_PROPERTY) Map<String, List<ApiIco>> ico) {
        super(ico);
    }

    public boolean isEmpty() {
        return ico == null || ico.isEmpty();
    }

    public List<ApiIco> getLiveIcos() {
        return ico.get(JSON_LIVE_ICO_PROPERTY);
    }

    public List<ApiIco> getUpcomingIcos() {
        return ico.get(JSON_UPCOMING_ICO_PROPERTY);
    }

    public List<ApiIco> getFinishedIcos() {
        return ico.get(JSON_FINISHED_ICO_PROPERTY);
    }
}
