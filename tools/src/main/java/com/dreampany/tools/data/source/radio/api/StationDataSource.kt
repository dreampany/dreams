package com.dreampany.tools.data.source.radio.api

import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.model.radio.Station

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationDataSource {
    @Throws
    suspend fun getItemsOfCountry(
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long,
        limit: Long
    ): List<Station>?

    @Throws
    suspend fun getItemsOfTrends(limit: Long): List<Station>?

    @Throws
    suspend fun getItemsOfPopular(limit: Long): List<Station>?

}