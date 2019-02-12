package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.misc.StateMapper;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.source.pref.Pref;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by Roman-372 on 2/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
public class ApiRepository {

    private final RxMapper rx;
    private final ResponseMapper rm;
    private final Pref pref;
    private final CoinMapper coinMapper;
    private final StateMapper stateMapper;
    private final CoinRepository coinRepo;
    private final StateRepository stateRepo;
    private final Map<Coin, Boolean> flags;

    @Inject
    ApiRepository(RxMapper rx,
                  ResponseMapper rm,
                  Pref pref,
                  CoinMapper coinMapper,
                  StateMapper stateMapper,
                  CoinRepository coinRepo,
                  StateRepository stateRepo) {
        this.rx = rx;
        this.rm = rm;
        this.pref = pref;
        this.coinMapper = coinMapper;
        this.stateMapper = stateMapper;
        this.coinRepo = coinRepo;
        this.stateRepo = stateRepo;
        flags = Maps.newConcurrentMap();
    }
}
