package com.dreampany.share.data.source.room.share;

import com.annimon.stream.Stream;
import com.dreampany.framework.data.model.State;
import com.dreampany.framework.data.source.repository.StateRepository;
import com.dreampany.media.data.enums.ItemSubtype;
import com.dreampany.media.data.enums.ItemType;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.source.api.MediaDataSource;
import com.dreampany.share.data.enums.ItemState;
import com.dreampany.share.data.misc.ImageStateMapper;
import com.dreampany.share.data.source.api.ShareDataSource;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ImageRoomShareDataSource implements ShareDataSource<Image> {

    private final String TYPE = ItemType.MEDIA.name();
    private final String SUBTYPE = ItemSubtype.IMAGE.name();
    private final String STATE_SHARED = ItemState.SHARED.name();

    private final ImageStateMapper mapper;
    private final StateRepository stateRepo;
    private final Map<Image, Boolean> shares;

    public ImageRoomShareDataSource(ImageStateMapper mapper,
                                    StateRepository stateRepo) {
        this.mapper = mapper;
        this.stateRepo = stateRepo;
        shares = Maps.newConcurrentMap();
    }

    @Override
    public ShareDataSource<Image> getThis() {
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
    public boolean isShared(Image image) {
        if (shares.containsKey(image)) {
            return shares.get(image);
        }
        return isExists(image);
    }

    @Override
    public Maybe<Boolean> isSharedRx(Image image) {
        if (shares.containsKey(image)) {
            return Maybe.fromCallable(() -> shares.get(image));
        }
        return isExistsRx(image);
    }

    @Override
    public long putShare(Image image) {
        State state = stateRepo.getItem(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return stateRepo.putItem(state);
    }

    @Override
    public Maybe<Long> putShareRx(Image image) {
        Maybe<State> Maybe = stateRepo.getItemRx(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return Maybe.map(stateRepo::putItem);
    }

    @Override
    public int getCount() {
        return stateRepo.getCount(TYPE, SUBTYPE, STATE_SHARED);
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return stateRepo.getCountRx(TYPE, SUBTYPE, STATE_SHARED);
    }

    @Override
    public boolean isExists(Image image) {
        State state = stateRepo.getItem(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        boolean shared = stateRepo.isExists(state);
        shares.put(image, shared);
        return shared;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Image image) {
        Maybe<State> Maybe = stateRepo.getItemRx(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return Maybe.map(state -> {
            boolean shared = stateRepo.isExists(state);
            shares.put(image, shared);
            return shared;
        });
    }

    @Override
    public long putItem(Image image) {
        State state = stateRepo.getItem(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        long result =  stateRepo.putItem(state);
        if (result > 0) {
            shares.put(image, true);
        }
        return result;
    }

    @Override
    public Maybe<Long> putItemRx(Image image) {
        Maybe<State> state = stateRepo.getItemRx(image.getId(), TYPE, SUBTYPE, STATE_SHARED);
        return state.map(stateRepo::putItem);
    }

    @Override
    public List<Long> putItems(List<Image> images) {
        List<State> states = new ArrayList<>(images.size());
        Stream.of(images).forEach(image -> states.add(stateRepo.getItem(image.getId(), TYPE, SUBTYPE, STATE_SHARED)));
        List<Long> result = stateRepo.putItems(states);
        if (images.size() == result.size()) {
            Stream.of(images).forEach(image -> shares.put(image, true));
        }
        return result;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Image> images) {
        return Maybe.fromCallable(() -> putItems(images));
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
        return null;
    }

    @Override
    public Maybe<List<Image>> getItemsRx() {
        return null;
    }

    @Override
    public List<Image> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Image>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public List<Image> getSharedItems(MediaDataSource<Image> source) {
        return null;
    }

    @Override
    public Maybe<List<Image>> getSharedItemsRx(MediaDataSource<Image> source) {
        return stateRepo.getItemsRx(TYPE, SUBTYPE, STATE_SHARED)
                .flatMap((Function<List<State>, MaybeSource<List<Image>>>) states -> getItemsRx(source, states));
    }

    private Maybe<List<Image>> getItemsRx(MediaDataSource<Image> source, List<State> states) {
        return Flowable.fromIterable(states)
                .map(item -> mapper.toItem(item, source))
                .toList()
                .toMaybe();
    }
}
