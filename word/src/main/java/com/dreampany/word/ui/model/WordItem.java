package com.dreampany.word.ui.model;

import androidx.annotation.LayoutRes;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.ColorUtil;
import com.dreampany.word.R;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.google.common.base.Objects;
import com.like.LikeButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class WordItem extends BaseItem<Word, WordItem.ViewHolder> {

    private Set<Pair<ItemState, ItemSubstate>> states;
    private boolean recent;
    private Map<String, String> translates;
    private boolean flagged;
    private long time;

    private WordItem(Word word, @LayoutRes int layoutId) {
        super(word, layoutId);
        states = new HashSet<>();
        translates = new HashMap<>();
    }

    public static WordItem getSimpleItem(Word item) {
        return new WordItem(item, R.layout.item_word);
    }

    public void addState(ItemState state, ItemSubstate substate) {
        states.add(Pair.create(state, substate));
    }

    public boolean hasState(ItemState state, ItemSubstate substate) {
        return states.contains(Pair.create(state, substate));
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRecent() {
        return recent;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        WordItem item = (WordItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new SimpleViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return item.getWord().toLowerCase().startsWith(((String) constraint).toLowerCase());
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        final WordAdapter adapter;

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            this.adapter = (WordAdapter) adapter;
        }

        abstract void bind(int position, WordItem item);
    }

    static final class SimpleViewHolder extends ViewHolder {

        @BindView(R.id.text_word)
        TextView word;
        @BindView(R.id.text_part_of_speech)
        TextView partOfSpeech;
        @BindView(R.id.text_pronunciation)
        TextView pronunciation;
        @BindView(R.id.button_like)
        LikeButton like;

        SimpleViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
            word.setOnClickListener(super.adapter.getClickListener());
            like.setOnClickListener(super.adapter.getClickListener());
        }

        @Override
        void bind(int position, WordItem item) {
            Word word = item.getItem();
            this.word.setText(word.getWord());
            this.partOfSpeech.setText(word.getPartOfSpeech());
            this.pronunciation.setText(word.getPronunciation());

            int color = item.hasState(ItemState.STATE, ItemSubstate.FULL) ? R.color.black : R.color.material_grey500;
            this.word.setTextColor(ColorUtil.getColor(getContext(), color));
            like.setTag(word);
            like.setLiked(item.isFlagged());
        }
    }
}
