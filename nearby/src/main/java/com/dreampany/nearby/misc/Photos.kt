package com.dreampany.nearby.misc

import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * Created by roman on 27/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun SimpleDraweeView.setUrl(url: String?): SimpleDraweeView {
    val uri = url?.toUri() ?: return this
    val request =
        ImageRequestBuilder.newBuilderWithSource(uri)
            //.setResizeOptions(new ResizeOptions(width, height))
            .build()
    this.setController(
        Fresco.newDraweeControllerBuilder()
            .setOldController(getController())
            .setImageRequest(request)
            //.setTapToRetryEnabled(retryEnabled)
            .build()
    )
    return this
}

fun SimpleDraweeView.setRes(@DrawableRes resId: Int): SimpleDraweeView {
    this.setImageResource(resId)
    return this
}