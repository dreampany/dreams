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

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class IcoMapper {

    private final SmartMap<Long, Ico> map;
    private final SmartCache<Long, Ico> cache;

    @Inject
    IcoMapper(@IcoAnnote SmartMap<Long, Ico> map,
              @IcoAnnote SmartCache<Long, Ico> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Ico toIco(ApiIco in, IcoStatus status) {
        if (in == null) {
            return null;
        }

        long id = DataUtil.getSha512(in.getIcoWatchListUrl());
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
