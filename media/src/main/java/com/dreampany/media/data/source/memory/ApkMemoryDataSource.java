
package com.dreampany.media.data.source.memory;

import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;


/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class ApkMemoryDataSource implements MediaDataSource<Apk> {

    private final ApkProvider provider;

    public ApkMemoryDataSource(ApkProvider provider) {
        this.provider = provider;
    }

    @Override
    public MediaDataSource<Apk> getThis() {
        return this;
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
    public boolean isExists(Apk apk) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Apk apk) {
        return null;
    }

    @Override
    public long putItem(Apk apk) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Apk apk) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Apk> apks) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Apk> apks) {
        return null;
    }

    @Override
    public Apk getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Apk> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Apk> getItems() {
        return provider.getItems();
    }

    @Override
    public Maybe<List<Apk>> getItemsRx() {
        return provider.getItemsRx();
    }

    @Override
    public List<Apk> getItems(int limit) {
        return provider.getItems(limit);
    }

    @Override
    public Maybe<List<Apk>> getItemsRx(int limit) {
        return provider.getItemsRx(limit);
    }
}