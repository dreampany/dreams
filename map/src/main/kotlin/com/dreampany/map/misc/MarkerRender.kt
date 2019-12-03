package com.dreampany.map.misc

import android.content.Context
import com.dreampany.map.ui.model.MarkerItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * Created by roman on 2019-12-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MarkerRender(context: Context, map: GoogleMap, cluster: ClusterManager<MarkerItem>) : DefaultClusterRenderer<MarkerItem>(context, map, cluster) {


    override fun onBeforeClusterItemRendered(item: MarkerItem, options: MarkerOptions) {
        options.title(item.title)
        options.snippet(item.snippet)
        options.icon(BitmapDescriptorFactory.fromBitmap(item.bitmap))
        super.onBeforeClusterItemRendered(item, options)
    }
}