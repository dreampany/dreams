package com.dreampany.media.data.source.room;

import com.dreampany.media.data.misc.ImageMapper;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class ImageRoomDataSource implements MediaDataSource<Image> {

    private final ImageMapper mapper;
    private final ImageDao dao;

    public ImageRoomDataSource(ImageMapper mapper,
                               ImageDao dao) {
        this.mapper = mapper;
        this.dao = dao;
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
        return dao.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return dao.getCountRx();
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
        return dao.insertOrReplace(image);
    }

    @Override
    public Maybe<Long> putItemRx(Image image) {
        return Maybe.fromCallable(() -> putItem(image));
    }

    @Override
    public List<Long> putItems(List<Image> images) {
        return dao.insertOrReplace(images);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Image> images) {
        return Maybe.fromCallable(() -> putItems(images));
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
        return dao.getItem(id);
    }

    @Override
    public Maybe<Image> getItemRx(long id) {
        return dao.getItemRx(id);
    }

    @Override
    public List<Image> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<Image>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<Image> getItems(int limit) {
        return dao.getItems(limit);
    }

    @Override
    public Maybe<List<Image>> getItemsRx(int limit) {
        return dao.getItemsRx(limit);
    }
}
