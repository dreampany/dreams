package com.dreampany.map.data.model

import com.dreampany.map.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-12-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GooglePlace(
    val id: String,
    @SerializedName(value = Constants.Keys.GooglePlace.PLACE_ID)
    val placeId: String,
    val name: String,
    val icon: String,
    val geometry: Geometry,
    val photos: List<PlacePhoto>
) {
}