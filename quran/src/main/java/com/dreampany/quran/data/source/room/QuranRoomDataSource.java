package com.dreampany.quran.data.source.room;

import com.dreampany.quran.data.model.Surah;
import com.dreampany.quran.data.source.api.QuranDataSource;

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
public class QuranRoomDataSource implements QuranDataSource {

    public QuranRoomDataSource() {

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
        return null;
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
