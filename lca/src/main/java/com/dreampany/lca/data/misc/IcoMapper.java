package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.iwl.model.ApiIco;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.misc.IcoAnnote;

import javax.inject.Inject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class IcoMapper {

    private final SmartMap<String, Ico> map;
    private final SmartCache<String, Ico> cache;

    @Inject
    IcoMapper(@IcoAnnote SmartMap<String, Ico> map,
              @IcoAnnote SmartCache<String, Ico> cache) {
        this.map = map;
        this.cache = cache;
    }

    public void clear(IcoStatus status) {
        Iterator<Map.Entry<String, Ico>> it = map.getIterator();
        while (it.hasNext()) {
            Map.Entry<String, Ico> entry = it.next();
            if (status.equals(entry.getValue().getStatus())) {
                it.remove();
            }
        }
    }

    public Ico toIco(ApiIco in, IcoStatus status) {
        if (in == null) {
            return null;
        }

        String id = in.getIcoWatchListUrl();
        Ico out = map.get(id);
        if (out == null) {
            out = new Ico();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setName(in.getName());
        out.setImageUrl(in.getImageUrl());
        out.setDescription(in.getDescription());
        out.setWebsiteLink(in.getWebsiteLink());
        out.setIcoWatchListUrl(in.getIcoWatchListUrl());
        out.setStartTime(in.getStartTime());
        out.setEndTime(in.getEndTime());
        out.setTimezone(in.getTimezone());
        out.setCoinSymbol(in.getCoinSymbol());
        out.setPriceUSD(in.getPriceUSD());
        out.setAllTimeRoi(in.getAllTimeRoi());
        out.setStatus(status);
        return out;
    }
}
