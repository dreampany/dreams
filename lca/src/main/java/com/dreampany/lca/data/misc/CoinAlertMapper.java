package com.dreampany.lca.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
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

    private final SmartMap<String, CoinAlert> map;
    private final SmartCache<String, CoinAlert> cache;
    private final Map<String, CoinAlert> alerts;

    @Inject
    CoinAlertMapper(@CoinAlertAnnote SmartMap<String, CoinAlert> map,
                    @CoinAlertAnnote SmartCache<String, CoinAlert> cache) {
        this.map = map;
        this.cache = cache;
        this.alerts = Maps.newConcurrentMap();
    }

    public boolean hasCoinAlerts() {
        return !map.isEmpty();
    }

    public boolean hasCoinAlert(String id) {
        return alerts.containsKey(id);
    }

    public boolean hasCoinAlert(String id, double dayChange) {
        return map.contains(id);
    }

    public void add(CoinAlert alert) {
        this.add(alert.getId(), alert);
    }

    public void add(String id, CoinAlert alert) {
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

    public CoinAlert toItem(String coinId, double priceUp, double priceDown, boolean full) {
        CoinAlert out = map.get(coinId);
        if (out == null) {
            out = new CoinAlert();
            if (full) {
                map.put(coinId, out);
            }
        }
        out.setId(coinId);
        out.setTime(TimeUtil.currentTime());
        out.setPriceUp(priceUp);
        out.setPriceDown(priceDown);
        return out;
    }

}
