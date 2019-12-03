package com.dreampany.map.misc

/**
 * Created by roman on 2019-11-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Api {
        object Map {
            const val BASE_URL = "https://maps.googleapis.com/maps/api/"
            const val PLACE_NEARBY_SEARCH = "place/nearbysearch/json"
        }
    }

    object Keys {
        object Response {
            const val STATUS = "status"
            const val RESULTS = "results"
        }

        object Map {
            const val KEY = "key"
            const val LOCATION = "location"
            const val RADIUS = "radius"
        }

        object GooglePlace {
            const val LATITUDE = "lat"
            const val LONGITUDE = "lng"
            const val PLACE_ID = "place_id";
            const val PHOTOS = "photos";
            const val PHOTO_REFERENCE = "photo_reference";
        }

    }
}