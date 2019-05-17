
package com.dreampany.media.data.source.memory;

import com.dreampany.media.data.model.Image;
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
public class ImageMemoryDataSource implements MediaDataSource<Image> {

    private final ImageProvider provider;

    public ImageMemoryDataSource(ImageProvider provider) {
        this.provider = provider;
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
    public boolean isExists(Image image) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Image image) {
        return null;
    }

    @Override
    public long putItem(Image image) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Image image) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Image> images) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Image> images) {
        return null;
    }

    @Override
    public int delete(Image image) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Image image) {
        return null;
    }

    @Override
    public List<Long> delete(List<Image> images) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Image> images) {
        return null;
    }

    @Override
    public Image getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Image> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Image> getItems() {
        return provider.getItems();
    }

    @Override
    public Maybe<List<Image>> getItemsRx() {
        return provider.getItemsRx();
    }

    @Override
    public List<Image> getItems(int limit) {
        return provider.getItems(limit);
    }

    @Override
    public Maybe<List<Image>> getItemsRx(int limit) {
        return provider.getItemsRx(limit);
    }
}