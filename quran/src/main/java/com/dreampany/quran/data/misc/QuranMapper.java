package com.dreampany.quran.data.misc;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.dreampany.frame.data.enums.Language;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.quran.data.model.Ayah;
import com.dreampany.quran.data.model.Surah;
import com.dreampany.quran.data.source.pref.Pref;
import com.dreampany.quran.misc.AyahAnnote;
import com.dreampany.quran.misc.SurahAnnote;

import org.jqurantree.orthography.Chapter;
import org.jqurantree.orthography.Verse;

import java.util.Iterator;
import java.util.Map;

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
        long id = DataUtil.getSha512(in.getChapterNumber());
        Surah out = surahMap.get(id);
        if (out == null) {
            out = new Surah();
            if (full) {
                surahMap.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setName(in.getName().toUnicode());
        out.setLanguage(Language.ARABIC);
        if (full) {
            bindAyahs(out, in.iterator(), full);
        }
        return out;
    }

    private void bindAyahs(Surah out, Iterator<Verse> verses, boolean full) {
        for (int index = 1; verses.hasNext(); index++) {
            Ayah ayah = toItem(verses.next(), index, full);
            out.addAyah(ayah);
        }
    }

    private Ayah toItem(Verse in, int numberInSurah, boolean full) {
        if (in == null) {
            return null;
        }
        long id = DataUtil.getSha512(in.getVerseNumber(), in.getChapterNumber());
        Ayah out = ayahMap.get(id);
        if (out == null) {
            out = new Ayah();
            if (full) {
                ayahMap.put(id, out);
            }
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setNumber(in.getVerseNumber());
        out.setNumberOfSurah(in.getChapterNumber());
        out.setNumberInSurah(numberInSurah);
        out.setLanguage(Language.ARABIC);
        out.setText(in.toUnicode());
        if (full) {

        }
        return out;
    }

}
