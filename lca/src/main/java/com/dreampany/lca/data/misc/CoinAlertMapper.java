package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.misc.CoinAlertAnnote;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinAlertMapper {

    private final SmartMap<Long, CoinAlert> map;
    private final SmartCache<Long, CoinAlert> cache;

    @Inject
    CoinAlertMapper(@CoinAlertAnnote SmartMap<Long, CoinAlert> map,
                    @CoinAlertAnnote SmartCache<Long, CoinAlert> cache) {
        this.map = map;
        this.cache = cache;
    }

    public boolean hasCoinAlerts() {
        return !map.isEmpty();
    }

    public boolean hasCoinAlert(String symbol, double dayChange) {
        long id = DataUtil.getSha512(symbol, String.valueOf(dayChange));
        return map.contains(id);
    }

    public void add(CoinAlert alert) {
        long id = DataUtil.getSha512(alert.getSymbol(), String.valueOf(alert.getDayChange()));
        this.add(id, alert);
    }

    public void add(long id, CoinAlert alert) {
        map.put(id, alert);
    }

    public void add(List<CoinAlert> alerts) {
        if (!DataUtil.isEmpty(alerts)) {
            for (CoinAlert coin : alerts) {
                add(coin);
            }
        }
    }

}
