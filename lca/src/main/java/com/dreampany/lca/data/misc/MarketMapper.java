package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cc.model.CcMarket;
import com.dreampany.lca.data.model.Market;
import com.dreampany.lca.misc.MarketAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MarketMapper {

    private final SmartMap<String, Market> map;
    private final SmartCache<String, Market> cache;

    @Inject
    MarketMapper(@MarketAnnote SmartMap<String, Market> map,
                 @MarketAnnote SmartCache<String, Market> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Market toMarket(CcMarket in) {
        if (in == null) {
            return null;
        }

        String id  = DataUtil.concat(in.getMarket(), in.getFromSymbol(), in.getToSymbol());
        Market out = map.get(id);
        if (out == null) {
            out = new Market();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setMarket(in.getMarket());
        out.setFromSymbol(in.getFromSymbol());
        out.setToSymbol(in.getToSymbol());
        out.setPrice(in.getPrice());
        out.setVolume24h(in.getVolume24h());
        out.setChangePct24h(in.getChangePCT24h());
        out.setChange24h(in.getChange24h());
        return out;
    }
}
