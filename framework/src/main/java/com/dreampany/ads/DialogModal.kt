package com.dreampany.ads

import java.lang.Float.parseFloat

/**
 * Created by roman on 16/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DialogModal {
    var iconUrl: String? = null
    var appTitle: String? = null
    var appDesc: String? = null
    var largeImageUrl: String? = null
    var packageOrUrl: String? = null
    var callToActionButtonText: String? = null
    var price: String? = null
    private var rating: String? = null

    fun setRating(ratings: String) {
        this.rating = ratings
    }

    fun getRating(): Float {
        var ratings = 0f
        if (rating != null && rating!!.isNotEmpty()) ratings = parseFloat(rating!!)
        return ratings
    }
}