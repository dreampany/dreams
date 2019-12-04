package com.dreampany.map.manager

import android.graphics.Bitmap
import com.dreampany.map.data.model.GooglePlace
import com.dreampany.map.data.model.GooglePlacesResponse
import com.dreampany.map.data.model.Location
import com.dreampany.map.data.remote.RemoteServiceManager
import com.dreampany.map.data.service.MapService
import com.dreampany.map.misc.Constants
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.common.collect.Maps
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*


/**
 * Created by roman on 2019-11-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
object PlaceManager {

    interface PlaceCallback {
        fun onPlaces(places: List<GooglePlace>)
        fun onPlacePhoto(place: GooglePlace, bitmap: Bitmap)
    }

    private val places: MutableMap<String, GooglePlace>
    private val bitmaps: MutableMap<String, Bitmap>

    init {
        places = Maps.newConcurrentMap()
        bitmaps = Maps.newConcurrentMap()
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
                        this.forEach {
                            places.put(it.placeId, it)
                        }
                        callback.onPlaces(this)
                    }

            }

        })
    }

    @Synchronized
    fun loadPhoto(client: PlacesClient, placeId: String, callback: PlaceCallback) {
        if (places.containsKey(placeId) && bitmaps.containsKey(placeId)) {
            val place = places.get(placeId)
            val bitmap = bitmaps.get(placeId)
            if (place != null && bitmap != null && !bitmap.isRecycled) {
                callback.onPlacePhoto(place, bitmap)
                return
            }
        }
        val fields: List<Place.Field> = Arrays.asList(Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)
        client.fetchPlace(placeRequest).addOnSuccessListener { response ->
            val place = response.place
            val meta = place.photoMetadatas?.first() ?: return@addOnSuccessListener
            val attr = meta.attributions
            val photoRequest = FetchPhotoRequest.builder(meta).build()
            client.fetchPhoto(photoRequest).addOnSuccessListener { photoResponse ->
                places.get(placeId)?.run {
                    var bitmap: Bitmap? = photoResponse.getBitmap()
                    bitmap = Constants.Api.resize(bitmap, 150, 150)
                    if (bitmap != null) {
                        bitmaps.put(placeId, bitmap)
                        callback.onPlacePhoto(this, bitmap)
                    }
                }
            }
        }
    }

    @Synchronized
    fun getBitmap(placeId: String): Bitmap? {
        return bitmaps.get(placeId)
    }
}