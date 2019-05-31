package com.dreampany.word.data.source.repository;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import com.google.common.collect.Maps;
import io.reactivex.Maybe;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
    private final Map<Word, Boolean> flags;

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
        flags = Maps.newConcurrentMap();
    }

    public List<Word> getCommonWords() {
        return wordRepo.getCommonItems();
    }

    public List<Word> getAlphaWords() {
        return wordRepo.getAlphaItems();
    }

    public Word getItem(String word, boolean full) {
        return wordRepo.getItem(word, full);
    }

    public Word getItemIf(Word word) {
        Word result = getRoomItemIf(word);
        if (result == null) {
            result = getFirestoreItemIf(word);
        }
        if (result == null) {
            result = getRemoteItemIf(word);
        }
        return result;
    }

    public Maybe<Word> getItemIfRx(Word word) {
        return Maybe.fromCallable(() -> getItemIf(word));
    }

    public List<Word> getSearchItems(String query, int limit) {
        return wordRepo.getSearchItems(query, limit);
    }

    public long putItem(Word word, ItemSubtype subtype, ItemState state) {
        long result = wordRepo.putItem(word);
        if (result != -1) {
            result = putState(word, subtype, state);
        }
        return result;
    }

    public boolean hasState(long id, ItemType type, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCount(id, type.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    public boolean hasState(Word word, ItemSubtype subtype) {
        boolean stated = stateRepo.getCount(word.getId(), ItemType.WORD.name(), subtype.name()) > 0;
        return stated;
    }

    public boolean hasState(Word word, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCount(word.getId(), ItemType.WORD.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    public long putState(Word word, ItemSubtype subtype, ItemState state) {
        State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        long result = stateRepo.putItem(s);
        return result;
    }

    public int removeState(Word word, ItemSubtype subtype, ItemState state) {
        State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        int result = stateRepo.delete(s);
        return result;
    }

    public int getStateCount(ItemType type, ItemSubtype subtype, ItemState state) {
        return stateRepo.getCount(type.name(), subtype.name(), state.name());
    }

    public List<State> getStates(Word word) {
        return stateRepo.getItems(word.getId(), ItemType.WORD.name(), ItemSubtype.DEFAULT.name());
    }

    public boolean isFlagged(Word word) {
        if (!flags.containsKey(word)) {
            boolean flag = hasState(word, ItemSubtype.DEFAULT, ItemState.FLAG);
            flags.put(word, flag);
        }
        return flags.get(word);
    }

    public boolean toggleFlag(Word word) {
        if (isFlagged(word)) {
            removeState(word, ItemSubtype.DEFAULT, ItemState.FLAG);
            flags.put(word, false);
        } else {
            putState(word, ItemSubtype.DEFAULT, ItemState.FLAG);
            flags.put(word, true);
        }
        return flags.get(word);
    }

    private Word getRoomItemIf(Word word) {
        if (!hasState(word, ItemSubtype.DEFAULT, ItemState.FULL)) {
            return null;
        }
        return wordRepo.getRoomItem(word.getWord(), true);
    }

    private Word getFirestoreItemIf(Word word) {
        Word result = wordRepo.getFirestoreItem(word.getWord(), true);
        if (result != null) {
            Timber.v("Firestore result success");
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL);
        }
        return result;
    }

    private Word getRemoteItemIf(Word word) {
        Word result = wordRepo.getRemoteItem(word.getWord(), true);
        if (result != null) {
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL);
            wordRepo.putFirestoreItem(result);
        }
        return result;
    }
}
