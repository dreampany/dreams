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
            const val API_KEY = "AIzaSyAohrGDTlSXg42OSlP3U2IvXmWtUiGcsbY"
            const val BASE_URL = "https://maps.googleapis.com/maps/api/"
            const val PLACE_NEARBY_SEARCH = "place/nearbysearch/json"
        }

        fun join(left: Double, right: Double, sep: Char): String {
            return String.format("%f%s%f", left, sep, right)
        }
    }

    object Keys {
        object Separators {
            const val COMMA = ','
        }

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

    object  Property {
        const val NEARBY_RADIUS = 5000
    }
}