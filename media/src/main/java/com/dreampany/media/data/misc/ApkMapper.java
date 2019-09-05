package com.dreampany.media.data.misc;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.FileUtil;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.misc.ApkAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ApkMapper extends MediaMapper {

    private final SmartMap<Long, Apk> map;
    private final SmartCache<Long, Apk> cache;

    @Inject
    ApkMapper(@ApkAnnote SmartMap<Long, Apk> map,
              @ApkAnnote SmartCache<Long, Apk> cache) {
        this.map = map;
        this.cache = cache;
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public String[] getProjection() {
        return new String[0];
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }

    @Override
    public String getSortOrder() {
        return null;
    }

    public boolean isExists(Apk item) {
        return map.contains(item.getId());
    }

    public Apk toItem(ApplicationInfo in, PackageManager pm) {
        if (in == null) {
            return null;
        }
        long id = DataUtil.getSha512(in.packageName);
        Apk out = map.get(id);
        if (out == null) {
            out = new Apk();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setPackageName(in.packageName);
        out.setDisplayName(in.loadLabel(pm).toString());
        out.setUri(in.publicSourceDir);
        out.setMimeType(FileUtil.getMimeType(in.publicSourceDir));
        out.setSize(FileUtil.getFileSize(in.publicSourceDir));
        return out;
    }
}
