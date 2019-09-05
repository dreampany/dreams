package com.dreampany.lca.data.misc;

import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.api.cc.model.CcExchange;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.misc.ExchangeAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ExchangeMapper {

    private final SmartMap<String, Exchange> map;
    private final SmartCache<String, Exchange> cache;

    @Inject
    ExchangeMapper(@ExchangeAnnote SmartMap<String, Exchange> map,
                   @ExchangeAnnote SmartCache<String, Exchange> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Exchange toExchange(CcExchange in) {
        if (in == null) {
            return null;
        }
        String id = DataUtil.join(in.getExchange(), in.getFromSymbol(), in.getToSymbol());
        Exchange out = map.get(id);
        if (out == null) {
            out = new Exchange();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setExchange(in.getExchange());
        out.setFromSymbol(in.getFromSymbol());
        out.setToSymbol(in.getToSymbol());
        out.setVolume24h(in.getVolume24h());
        out.setVolume24hTo(in.getVolume24hTo());
        return out;
    }
}
