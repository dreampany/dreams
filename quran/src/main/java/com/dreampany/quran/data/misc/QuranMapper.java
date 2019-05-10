package com.dreampany.quran.data.misc;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.quran.data.model.Ayah;
import com.dreampany.quran.data.model.Surah;
import com.dreampany.quran.data.source.pref.Pref;
import com.dreampany.quran.misc.AyahAnnote;
import com.dreampany.quran.misc.SurahAnnote;

import org.jqurantree.orthography.Chapter;

import javax.inject.Inject;

/**
 * Created by Roman-372 on 5/9/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class QuranMapper {

    private final Pref pref;

    private final SmartMap<Long, Surah> surahMap;
    private final SmartCache<Long, Surah> surahCache;

    private final SmartMap<Long, Ayah> ayahMap;
    private final SmartCache<Long, Ayah> ayahCache;

    @Inject
    QuranMapper(
            Pref pref,
            @SurahAnnote SmartMap<Long, Surah> surahMap,
            @SurahAnnote SmartCache<Long, Surah> surahCache,
            @AyahAnnote SmartMap<Long, Ayah> ayahMap,
            @AyahAnnote SmartCache<Long, Ayah> ayahCache) {
        this.pref = pref;
        this.surahMap = surahMap;
        this.surahCache = surahCache;
        this.ayahMap = ayahMap;
        this.ayahCache = ayahCache;
    }

    public Surah toItem(Chapter in, boolean full) {
        if (in == null) {
            return null;
        }
        //long id = DataUtil.getSha512(source.value(), in.getId());
        return null;
    }
}
