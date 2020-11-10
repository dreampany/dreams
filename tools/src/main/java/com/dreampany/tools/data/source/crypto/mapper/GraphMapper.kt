package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.data.source.repo.TimeRepo
import com.dreampany.tools.api.crypto.model.CryptoGraph
import com.dreampany.tools.data.model.crypto.Graph
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class GraphMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val timeRepo: TimeRepo
) {

    private val graphs: MutableMap<String, Graph>

    init {
        graphs = Maps.newConcurrentMap()
    }

    @Synchronized
    fun read(input: CryptoGraph): Graph {
        var out: Graph? = graphs.get(input.slug)
        if (out == null) {
            out = Graph(input.slug)
            graphs.put(input.slug, out)
        }
        out.startTime = input.startTime
        out.endTime = input.endTime
        out.priceBtc = input.priceBtc
        out.priceUsd = input.priceUsd
        out.volumeUsd = input.volumeUsd

        return out
    }
}