package com.dreampany.map.misc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.dreampany.map.R
import com.dreampany.map.ui.model.MarkerItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.rishabhharit.roundedimageview.RoundedImageView
import timber.log.Timber


/**
 * Created by roman on 2019-12-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MarkerRender(context: Context, map: GoogleMap, cluster: ClusterManager<MarkerItem>) :
    DefaultClusterRenderer<MarkerItem>(context, map, cluster) {

    private val context: Context
    private val iconGenerator: IconGenerator
    private val icon: RoundedImageView

    init {
        this.context = context.applicationContext
        iconGenerator =  IconGenerator(this.context);
        val markerView: View = LayoutInflater.from(context).inflate(R.layout.item_icon, null)
        icon = markerView.findViewById(R.id.icon)
        iconGenerator.setContentView(markerView)

/*        markerView.setOnLongClickListener {
            Timber.v("Clicked")
            true
        }*/
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MarkerItem>): Boolean {
        return cluster.size > 1
    }

    override fun onBeforeClusterItemRendered(item: MarkerItem, options: MarkerOptions) {
        options.title(item.title)
        options.snippet(item.snippet)
        if (item.bitmap != null) {
            this.icon.setImageBitmap(item.bitmap)
            val icon = iconGenerator.makeIcon(item.title)
            options.icon(BitmapDescriptorFactory.fromBitmap(icon))
        }
        /*var bitmap: Bitmap? = Constants.Api.getBitmapMarker(
            context,
            R.drawable.ic_tutlip,
            item.title.first().toString()
        )
        bitmap = Constants.Api.resize(bitmap, 150, 150)
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        options.anchor(0.5f, 1f)*/
        super.onBeforeClusterItemRendered(item, options)
    }
}