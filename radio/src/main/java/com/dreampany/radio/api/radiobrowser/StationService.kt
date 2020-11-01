package com.dreampany.radio.api.radiobrowser

import retrofit2.Call
import retrofit2.http.*

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationService {

    @GET(value = Constants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_COUNTRY)
    fun readsByCountry(
        @Path(value = Constants.Keys.Station.COUNTRY_CODE) countryCode: String,
        @Query(value = Constants.Keys.Station.HIDE_BROKEN) hideBroken: Boolean,
        @Query(value = Constants.Keys.Station.ORDER) order: String,
        @Query(value = Constants.Keys.Station.REVERSE) reverse: Boolean,
        @Query(value = Constants.Keys.Station.OFFSET) offset: Long,
        @Query(value = Constants.Keys.Station.LIMIT) limit: Long
    ): Call<List<RadioStation>>

    @GET(value = Constants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_TRENDS)
    fun readsByTrend(@Path(value = Constants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

    @GET(value = Constants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_POPULAR)
    fun readsByPopular(@Path(value = Constants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

    /* @FormUrlEncoded
     @POST(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_SEARCH)
     fun getItemsOfCountryV2(@Path(value = Constants.Station.COUNTRY_CODE) countryCode: String) : Call<List<RadioStation>>
 */

}