package com.dreampany.map.data.service

import com.dreampany.map.data.model.GooglePlacesResponse
import com.dreampany.map.misc.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roman on 2019-11-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface MapService {

    @GET(Constants.Api.Map.PLACE_NEARBY_SEARCH)
    fun getNearbyPlaces(
        @Query(Constants.Keys.Map.KEY) key: String,
        @Query(Constants.Keys.Map.LOCATION) location: String,
        @Query(Constants.Keys.Map.RADIUS) radius: Int
    ): Call<GooglePlacesResponse>

}