package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.tools.data.model.Station
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationDataSource : DataSource<Station> {

    fun getItemsByCountryCode(countryCode: String) : List<Station>?

    fun getItemsByCountryCodeRx(countryCode: String) : Maybe<List<Station>>
}