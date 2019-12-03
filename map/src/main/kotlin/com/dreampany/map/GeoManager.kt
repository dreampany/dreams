package com.dreampany.map

import com.dreampany.map.data.model.GooglePlace
import com.dreampany.map.data.model.GooglePlacesResponse
import com.dreampany.map.data.model.Location
import com.dreampany.map.data.remote.RemoteServiceManager
import com.dreampany.map.data.service.MapService
import com.dreampany.map.misc.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by roman on 2019-11-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
object GeoManager {

    interface PlaceCallback {
        fun onPlaces(places: List<GooglePlace>)
    }

    @Synchronized
    fun nearbyPlaces(location: Location, callback: PlaceCallback) {
        val service = RemoteServiceManager.of(Constants.Api.Map.BASE_URL, MapService::class)
        val loc = Constants.Api.join(
            location.latitude,
            location.longitude,
            Constants.Keys.Separators.COMMA
        )
        service.getNearbyPlaces(
            loc,
            Constants.Property.NEARBY_RADIUS,
            Constants.Api.Map.API_KEY
        ).enqueue(object : Callback<GooglePlacesResponse> {
            override fun onFailure(call: Call<GooglePlacesResponse>, error: Throwable) {
                Timber.e(error)
            }

            override fun onResponse(
                call: Call<GooglePlacesResponse>,
                response: Response<GooglePlacesResponse>
            ) {
                if (response.isSuccessful)
                    response.body()?.places?.run {
                        callback.onPlaces(this)
                    }

            }

        })
    }
}