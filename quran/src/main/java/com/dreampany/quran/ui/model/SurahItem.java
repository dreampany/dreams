package com.dreampany.quran.ui.model;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.dreampany.frame.data.enums.Language;
import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.quran.R;
import com.dreampany.quran.data.model.Surah;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Roman-372 on 5/10/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class SurahItem extends BaseItem<Surah, SurahItem.ViewHolder> {

    private Language language;
    private Surah translatedSurah;
    private boolean favorite;

    private SurahItem(Surah surah, Language language, Surah translatedSurah, @LayoutRes int layoutId) {
        super(surah, layoutId);
        this.language = language;
        this.translatedSurah = translatedSurah;
    }

    public static SurahItem getItem(Surah surah, Language language, Surah translatedSurah) {
        return new SurahItem(surah, language,translatedSurah, R.layout.item_surah);
    }

    @Override
    public boolean equals(Object in) {
        return false;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return null;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        //   holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return false;
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }
    }
}
