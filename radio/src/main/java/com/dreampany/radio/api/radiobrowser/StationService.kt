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

    @GET(value = Constants.Apis.Radio.COUNTRY_CODE_EXACT)
    fun readsByCountry(
        @Path(value = Constants.Keys.Station.COUNTRY_CODE) countryCode: String,
        @Query(value = Constants.Keys.Station.ORDER) order: String,
        @Query(value = Constants.Keys.Station.OFFSET) offset: Long,
        @Query(value = Constants.Keys.Station.LIMIT) limit: Long
    ): Call<List<RadioStation>>

    @GET(value = Constants.Apis.Radio.TOP_CLICK)
    fun readsByTopClick(@Path(value = Constants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

    @GET(value = Constants.Apis.Radio.TOP_VOTE)
    fun readsByTopVote(@Path(value = Constants.Keys.Station.LIMIT) limit: Long): Call<List<RadioStation>>

     @FormUrlEncoded
     @POST(value = Constants.Apis.Radio.SEARCH)
     fun search(
         @Path(value = Constants.Keys.Station.COUNTRY_CODE) countryCode: String
     ) : Call<List<RadioStation>>

}