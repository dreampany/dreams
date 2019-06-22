package com.dreampany.word.data.source.repository;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import timber.log.Timber;

/**
 * Created by Roman on 1/30/2019
 * Copyright (c) 2019 Dreampany. All rights reserved.
 * dreampanymail@gmail.com
 * Last modified $file.lastModified
 */

@Singleton
public class ApiRepository {

    private final RxMapper rx;
    private final ResponseMapper rm;
    private final Pref pref;
    private final WordMapper mapper;
    private final StateMapper stateMapper;
    private final WordRepository wordRepo;
    private final StateRepository stateRepo;
    private final Map<Word, Boolean> favorites;

    @Inject
    ApiRepository(RxMapper rx,
                  ResponseMapper rm,
                  Pref pref,
                  WordMapper mapper,
                  StateMapper stateMapper,
                  WordRepository wordRepo,
                  StateRepository stateRepo) {
        this.rx = rx;
        this.rm = rm;
        this.pref = pref;
        this.mapper = mapper;
        this.stateMapper = stateMapper;
        this.wordRepo = wordRepo;
        this.stateRepo = stateRepo;
        favorites = Maps.newConcurrentMap();
    }

    synchronized public List<Word> getCommonWords() {
        return wordRepo.getCommonItems();
    }

    synchronized public List<Word> getAlphaWords() {
        return wordRepo.getAlphaItems();
    }

    synchronized public Word getItem(String word, boolean full) {
        return wordRepo.getItem(word, full);
    }

    synchronized public Word getItemIf(Word word) {
        Word result = getRoomItemIf(word);
        if (result == null) {
            result = getFirestoreItemIf(word);
        }
        if (result == null) {
            result = getRemoteItemIf(word);
        }
        return result;
    }

    synchronized public Maybe<Word> getItemIfRx(Word word) {
        return Maybe.fromCallable(() -> getItemIf(word));
    }

    synchronized public Maybe<List<String>> getAllRawWordsRx() {
        return wordRepo.getRawWordsRx();
    }

    synchronized public List<Word> getSearchItems(String query, int limit) {
        return wordRepo.getSearchItems(query, limit);
    }

    synchronized public long putItem(Word word, ItemSubtype subtype, ItemState state) {
        long result = wordRepo.putItem(word);
        if (result != -1) {
            result = putState(word, subtype, state);
        }
        return result;
    }

    synchronized public List<Long> putItems(List<Word> words, ItemSubtype subtype, ItemState state) {
        List<Long> result = wordRepo.putItems(words);
        if (DataUtil.isEqual(words, result)) {
            result = putStates(words, subtype, state);
        }
        return result;
    }

    synchronized public boolean hasState(String id, ItemType type, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCountById(id, type.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    synchronized public boolean hasState(Word word, ItemSubtype subtype) {
        boolean stated = stateRepo.getCountById(word.getId(), ItemType.WORD.name(), subtype.name()) > 0;
        return stated;
    }

    synchronized public boolean hasState(Word word, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCountById(word.getId(), ItemType.WORD.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    synchronized public long putState(Word word, ItemSubtype subtype, ItemState state) {
        State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        long result = stateRepo.putItem(s);
        return result;
    }

    synchronized public List<Long> putStates(List<Word> words, ItemSubtype subtype, ItemState state) {
        List<State> states = new ArrayList<>();
        for (Word word : words) {
            State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
            s.setTime(TimeUtil.currentTime());
            states.add(s);
        }
        List<Long> result = stateRepo.putItems(states);
        return result;
    }

    synchronized public int removeState(Word word, ItemSubtype subtype, ItemState state) {
        State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        int result = stateRepo.delete(s);
        return result;
    }

    synchronized public int getStateCount(ItemType type, ItemSubtype subtype, ItemState state) {
        return stateRepo.getCount(type.name(), subtype.name(), state.name());
    }

    synchronized public List<State> getStates(Word word) {
        return stateRepo.getItems(word.getId(), ItemType.WORD.name(), ItemSubtype.DEFAULT.name());
    }

    public List<Word> getFavorites() {
        List<State> states = stateRepo.getItems(ItemType.WORD.name(), ItemSubtype.DEFAULT.name(), ItemState.FAVORITE.name());
        return getItemsOfStatesIf(states);
    }

    public long putFavorite(Word word) {
        long result = putState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }

    public int removeFavorite(Word word) {
        int result = removeState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }

    synchronized public boolean isFavorite(Word word) {
        if (!favorites.containsKey(word)) {
            boolean favorite = hasState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE);
            favorites.put(word, favorite);
        }
        return favorites.get(word);
    }

    public boolean toggleFavorite(Word word) {
        boolean favorite = hasState(word, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        if (favorite) {
            removeFavorite(word);
            favorites.put(word, false);
        } else {
            putFavorite(word);
            favorites.put(word, true);
        }
        return favorites.get(word);
    }

    private Word getRoomItemIf(Word word) {
        if (!hasState(word, ItemSubtype.DEFAULT, ItemState.FULL)) {
            return null;
        }
        return wordRepo.getRoomItem(word.getId(), true);
    }

    public List<Word> getItemsIf(List<String> wordIds) {
        return wordRepo.getItemsRx(wordIds).blockingGet();
    }


    private Word getFirestoreItemIf(Word word) {
        Word result = wordRepo.getFirestoreItem(word.getId(), true);
        if (result != null) {
            Timber.v("Firestore result success");
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL);
        }
        return result;
    }

    private Word getRemoteItemIf(Word word) {
        Word result = wordRepo.getRemoteItem(word.getId(), true);
        if (result != null) {
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL);
            wordRepo.putFirestoreItem(result);
        }
        return result;
    }

    private List<Word> getItemsOfStatesIf(List<State> states) {
        if (DataUtil.isEmpty(states)) {
            return null;
        }
        List<Word> result = new ArrayList<>(states.size());
        Stream.of(states).forEach(state -> {
            Word item = mapper.toItem(state, wordRepo);
            if (item != null) {
                result.add(item);
            }
        });
        return result;
    }
}
