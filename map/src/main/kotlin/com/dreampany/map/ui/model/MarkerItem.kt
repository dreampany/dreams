package com.dreampany.map.ui.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by roman on 2019-12-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class MarkerItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String,
    val bitmap: Bitmap?
): ClusterItem {
    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }
}
