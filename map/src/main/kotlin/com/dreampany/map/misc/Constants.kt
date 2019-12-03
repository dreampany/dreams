package com.dreampany.map.misc

import android.content.Context
import android.content.res.Resources
import android.graphics.*


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

        fun getBitmapMarker(
            context: Context,
            resourceId: Int,
            text: String
        ): Bitmap? {
            return try {
                val resources: Resources = context.getResources()
                val scale: Float = resources.getDisplayMetrics().density
                var bitmap = BitmapFactory.decodeResource(resources, resourceId)
                var bitmapConfig = bitmap.config
                // set default bitmap config if none
                if (bitmapConfig == null) bitmapConfig = Bitmap.Config.ARGB_8888
                bitmap = bitmap.copy(bitmapConfig, true)
                val canvas = Canvas(bitmap)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.setColor(Color.WHITE)
                paint.setTextSize((14 * scale).toFloat())
                paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY)
                // draw text to the Canvas center
                val bounds = Rect()
                paint.getTextBounds(text, 0, text.length, bounds)
                val x: Int = (bitmap.width - bounds.width()) / 2
                val y: Int = (bitmap.height + bounds.height()) / 2
                canvas.drawText(text, x * scale, y * scale, paint)
                bitmap
            } catch (e: Exception) {
                null
            }
        }

         fun resize(image: Bitmap?, maxWidth: Int, maxHeight: Int): Bitmap? {
             if (image == null) return null
            var image = image
            return if (maxHeight > 0 && maxWidth > 0) {
                val width = image.width
                val height = image.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > 1) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
                image
            } else {
                image
            }
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

    object Property {
        const val NEARBY_RADIUS = 1000
        const val GROUND_OVERLAY_WIDTH = 5000f
        const val GROUND_OVERLAY_HEIGHT = 8000f
    }
}