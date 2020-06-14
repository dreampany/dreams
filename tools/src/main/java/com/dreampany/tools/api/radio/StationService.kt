package com.dreampany.tools.api.radio

import com.dreampany.tools.misc.constants.RadioConstants
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationService {

    @GET(value = RadioConstants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_COUNTRY)
    fun getItemsOfCountry(
        @Path(value = RadioConstants.Keys.Station.COUNTRY_CODE) countryCode: String,
        @Query(value = RadioConstants.Keys.Station.HIDE_BROKEN) hideBroken: Boolean,
        @Query(value = RadioConstants.Keys.Station.ORDER) order: String,
        @Query(value = RadioConstants.Keys.Station.REVERSE) reverse: Boolean,
        @Query(value = RadioConstants.Keys.Station.OFFSET) offset: Long,
        @Query(value = RadioConstants.Keys.Station.LIMIT) limit: Long
    ): Call<List<RadioStation>>

    @GET(value = RadioConstants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_TRENDS)
    fun getItemsOfTrends(@Path(value = RadioConstants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

    @GET(value = RadioConstants.Apis.Radio.RADIO_BROWSER_STATIONS_OF_POPULAR)
    fun getItemsOfPopular(@Path(value = RadioConstants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

    /* @FormUrlEncoded
     @POST(value = Constants.Api.Radio.RADIO_BROWSER_STATIONS_SEARCH)
     fun getItemsOfCountryV2(@Path(value = Constants.Station.COUNTRY_CODE) countryCode: String) : Call<List<RadioStation>>
 */

}