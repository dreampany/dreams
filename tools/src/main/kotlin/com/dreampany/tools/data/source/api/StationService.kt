package com.dreampany.tools.data.source.api

import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationService {

    @GET(value = Constants.Api.RADIO_BROWSER_STATIONS_BY_COUNTRY_CODE)
    fun getItemsByCountryCode(@Path(value = Constants.Station.COUNTRY_CODE) countryCode: String) : Call<List<Station>>
}