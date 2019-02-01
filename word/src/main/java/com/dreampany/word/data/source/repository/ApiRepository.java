package com.dreampany.word.data.source.repository;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.FlagRepository;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.Pref;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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
    private final WordRepository wordRepo;
    private final StateRepository stateRepo;
    private final FlagRepository flagRepo;

    @Inject
    ApiRepository(RxMapper rx,
                  ResponseMapper rm,
                  Pref pref,
                  WordMapper mapper,
                  WordRepository wordRepo,
                  StateRepository stateRepo,
                  FlagRepository flagRepo) {
        this.rx = rx;
        this.rm = rm;
        this.pref = pref;
        this.mapper = mapper;
        this.wordRepo = wordRepo;
        this.stateRepo = stateRepo;
        this.flagRepo = flagRepo;
    }

    public List<Word> getCommonWords() {
        return wordRepo.getCommonItems();
    }

    public List<Word> getAlphaWords() {
        return wordRepo.getAlphaItems();
    }

    public Maybe<List<Word>> getSearchItemsRx(String query, int limit) {
        return null;
    }

    public long putItem(Word word, ItemSubtype subtype, ItemState state) {
        long result = wordRepo.putItem(word);
        if (result != -1) {
            result = putState(word, subtype, state);
        }
        return result;
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

    public int getStateCount(ItemType type, ItemSubtype subtype, ItemState state) {
        return stateRepo.getCount(type.name(), subtype.name(), state.name());
    }
}
