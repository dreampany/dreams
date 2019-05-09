package com.dreampany.quran.data.source.assets;

import android.content.Context;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.dreampany.quran.data.misc.QuranMapper;
import com.dreampany.quran.data.model.Surah;
import com.dreampany.quran.data.source.api.QuranDataSource;

import org.jqurantree.orthography.Chapter;
import org.jqurantree.orthography.Document;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Roman-372 on 5/9/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
public class QuranSdkDataSource implements QuranDataSource {

    private final Context context;
    private final QuranMapper mapper;

    public QuranSdkDataSource(Context context,
                              QuranMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Surah surah) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Surah surah) {
        return null;
    }

    @Override
    public long putItem(Surah surah) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Surah surah) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Surah> surahs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Surah> surahs) {
        return null;
    }

    @Override
    public int delete(Surah surah) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Surah surah) {
        return null;
    }

    @Override
    public List<Long> delete(List<Surah> surahs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Surah> surahs) {
        return null;
    }

    @Override
    public Surah getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Surah> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Surah> getItems() {
        Iterable<Chapter> chapters = Document.getChapters();
        List<Surah> surahs = new ArrayList<>();
        Stream.of(chapters).forEach(chapter -> {

        });
        return surahs;
    }

    @Override
    public Maybe<List<Surah>> getItemsRx() {
        return null;
    }

    @Override
    public List<Surah> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Surah>> getItemsRx(int limit) {
        return null;
    }
}
