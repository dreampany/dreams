package com.dreampany.media.data.misc;

import android.net.Uri;

/**
 * Created by Hawladar Roman on 8/13/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public abstract class MediaMapper {
    public abstract Uri getUri();

    public  abstract String[] getProjection();

    public abstract String getSelection();

    public abstract String[] getSelectionArgs();

    public  abstract String getSortOrder();
}
