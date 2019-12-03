package com.dreampany.map.data.model

import com.dreampany.map.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GooglePlacesResponse(
    @SerializedName(value = Constants.Keys.Response.STATUS)
    val status: String,
    @SerializedName(value = Constants.Keys.Response.RESULTS)
    val places: List<GooglePlace>
) {}