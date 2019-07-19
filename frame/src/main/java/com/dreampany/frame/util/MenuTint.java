package com.dreampany.frame.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

/**
 * Created by Roman-372 on 6/7/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class MenuTint {

    /**
     * Sets the color filter and/or the alpha transparency on a {@link MenuItem}'s icon.
     *
     * @param menuItem
     *     The {@link MenuItem} to theme.
     * @param color
     *     The color to set for the color filter or {@code null} for no changes.
     * @param alpha
     *     The alpha value (0...255) to set on the icon or {@code null} for no changes.
     */
    public static void colorMenuItem(MenuItem menuItem, Integer color, Integer alpha) {
        if (AndroidUtil.hasOreo()) {
            //return;
        }
        if (color == null && alpha == null) {
            return; // nothing to do.
        }
        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
            drawable.mutate();
            if (color != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
            if (alpha != null) {
                drawable.setAlpha(alpha);
            }
        }
    }

    public static void colorMenuItem(Integer color, Integer alpha, MenuItem... items) {
        if (AndroidUtil.hasOreo()) {
            //return;
        }
        if (color == null && alpha == null) {
            return; // nothing to do.
        }
        for (MenuItem item : items) {
            Drawable drawable = item.getIcon();
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
                drawable.mutate();
                if (color != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
                if (alpha != null) {
                    drawable.setAlpha(alpha);
                }
            }
        }
    }
}
