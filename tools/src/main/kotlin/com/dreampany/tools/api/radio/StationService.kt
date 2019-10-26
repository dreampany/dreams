package com.dreampany.tools.api.radio

import com.dreampany.tools.misc.Constants
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationService {

    @GET(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_OF_COUNTRY)
    fun getItemsOfCountry(
        @Path(value = Constants.Station.COUNTRY_CODE) countryCode: String,
        @Path(value = Constants.Station.LIMIT) limit: Long
    ) : Call<List<RadioStation>>

    @GET(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_OF_TRENDS)
    fun getItemsOfTrends(@Path(value = Constants.Station.LIMIT) limit: Long) : Call<List<RadioStation>>

    @GET(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_OF_POPULAR)
    fun getItemsOfPopular(@Path(value = Constants.Station.LIMIT) limit: Long) : Call<List<RadioStation>>

   /* @FormUrlEncoded
    @POST(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_SEARCH)
    fun getItemsOfCountryV2(@Path(value = Constants.Station.COUNTRY_CODE) countryCode: String) : Call<List<RadioStation>>
*/

}