package com.dreampany.word.data.source.api;

import android.graphics.Bitmap;
import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.word.data.model.Word;
import io.reactivex.Maybe;

import java.util.List;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface WordDataSource extends DataSource<Word> {

    Word getTodayItem();

    Maybe<Word> getTodayItemRx();

    Word getItem(String word);

    Maybe<Word> getItemRx(String word);

    List<Word> getSearchItems(String query, int limit);

    List<Word> getCommonItems();

    List<Word> getAlphaItems();

    Maybe<List<Word>> getItemsRx(Bitmap bitmap);
}
