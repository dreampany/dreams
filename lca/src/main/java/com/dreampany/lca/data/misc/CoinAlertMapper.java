package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.misc.CoinAlertAnnote;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class CoinAlertMapper {

    private final SmartMap<Long, CoinAlert> map;
    private final SmartCache<Long, CoinAlert> cache;
    private final Map<String, CoinAlert> alerts;

    @Inject
    CoinAlertMapper(@CoinAlertAnnote SmartMap<Long, CoinAlert> map,
                    @CoinAlertAnnote SmartCache<Long, CoinAlert> cache) {
        this.map = map;
        this.cache = cache;
        this.alerts = Maps.newConcurrentMap();
    }

    public boolean hasCoinAlerts() {
        return !map.isEmpty();
    }

    public boolean hasCoinAlert(String symbol) {
        return alerts.containsKey(symbol);
    }

    public boolean hasCoinAlert(String symbol, double dayChange) {
        long id = DataUtil.getSha512(symbol);
        return map.contains(id);
    }

    public void add(CoinAlert alert) {
        long id = DataUtil.getSha512(alert.getSymbol());
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

    public CoinAlert getCoinAlert(String symbol) {
        return alerts.get(symbol);
    }

    public CoinAlert toItem(String symbol, double priceUp, double priceDown, boolean full) {
        if (DataUtil.isEmpty(symbol)) {
            return null;
        }
        long id = DataUtil.getSha512(symbol);
        CoinAlert out = map.get(id);
        if (out == null) {
            out = new CoinAlert();
            if (full) {
                map.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setSymbol(symbol);
        out.setPriceUp(priceUp);
        out.setPriceDown(priceDown);
        return out;
    }

}
