/*
package com.dreampany.lca.util;

import android.net.Uri;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

*/
/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

public final class FrescoUtil {
    private FrescoUtil() {}

    public static void loadResource(SimpleDraweeView view, int resourceId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(resourceId))
                .build();
        view.setImageURI(uri);
    }

    public static void loadImage(SimpleDraweeView view, String uri, int size) {
        loadImage(view, uri, size, size);
    }

    public static void loadImage(SimpleDraweeView view, String uri, int width, int height) {
        if (uri == null) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        view.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(view.getController())
                        .setImageRequest(request)
                        .build());
    }

    public static void loadImage(SimpleDraweeView view, String uri) {
        if (uri == null) {
            return;
        }
        view.setImageURI(uri);
    }
}
*/
