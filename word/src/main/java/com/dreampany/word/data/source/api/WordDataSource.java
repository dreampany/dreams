package com.dreampany.word.data.source.api;

import android.graphics.Bitmap;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.DataSource;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.model.Word;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface WordDataSource extends DataSource<Word> {

    boolean hasState(Word word, ItemState state);

    boolean hasState(Word word, ItemState state, ItemSubstate substate);

    int getStateCount(ItemState state, ItemSubstate substate);

    List<State> getStates(Word word);

    List<State> getStates(Word word, ItemState state);

    long putItem(Word word,  ItemState state);

    long putItem(Word word,  ItemState state, ItemSubstate substate);

    long putItem(Word word, ItemState state, boolean replaceable);

    Maybe<Long> putItemRx(Word word, ItemState state);

    Maybe<Long> putItemRx(Word word, ItemState state, ItemSubstate substate);

    long putState(Word word, ItemState state);

    long putState(Word word, ItemState state, ItemSubstate substate);

    Maybe<Long> putStateRx(Word word, ItemState state);

    Word getTodayItem();

    Maybe<Word> getTodayItemRx();

    Word getItem(String word);

    Maybe<Word> getItemRx(String word);

    Maybe<List<Word>> getSearchItemsRx(String query);

    Maybe<List<Word>> getSearchItemsRx(String query, int limit);

    List<Word> getCommonItems();

    List<Word> getAlphaItems();

    Maybe<List<Word>> getItemsRx(Bitmap bitmap);
}
