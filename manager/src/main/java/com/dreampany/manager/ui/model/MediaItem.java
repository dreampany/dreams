package com.dreampany.manager.ui.model;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.dreampany.framework.ui.model.BaseItem;
import com.dreampany.framework.util.AndroidUtil;
import com.dreampany.framework.util.DisplayUtil;
import com.dreampany.framework.util.FrescoUtil;
import com.dreampany.framework.util.NumberUtil;
import com.dreampany.manager.R;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.model.Media;
import com.dreampany.manager.ui.adapter.MediaAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;
import com.like.LikeButton;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import timber.log.Timber;

/**
 * Created by Roman-372 on 5/14/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class MediaItem extends BaseItem<Media, MediaItem.ViewHolder> {

    private boolean selected;
    private boolean favorite;
    private boolean shared;

    private MediaItem(@NonNull Media item, @LayoutRes int layoutId) {
        super(item, layoutId);
    }

    public static MediaItem getItem(@NonNull Media item) {
        switch (item.getMediaType()) {
            case APK:
                return new MediaItem(item, R.layout.item_apk);
            case IMAGE:
            default:
                return new MediaItem(item, R.layout.item_image);
        }
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        MediaItem item = (MediaItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        MediaType type = getItem().getMediaType();
        switch (type) {
            case APK:
                return new ApkViewHolder(view, adapter);
            case IMAGE:
            default:
                return new ImageViewHolder(view, adapter);
        }
    }

    @Override
    public boolean filter(Serializable constraint) {
        return item.getDisplayName().toLowerCase().startsWith(((String) constraint).toLowerCase());
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isShared() {
        return shared;
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {
        MediaAdapter adapter;
        @BindView(R.id.image_icon)
        SimpleDraweeView icon;
        @BindView(R.id.text_size)
        TextView size;
        @BindView(R.id.button_like)
        LikeButton like;

        ViewHolder(@NonNull View view, @NonNull MediaAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            this.adapter = adapter;
            like.setOnClickListener(this.adapter.getClickListener());
        }

        void bind(int position, MediaItem item) {
            Media media = item.getItem();
            size.setText(NumberUtil.readableFileSize(media.getSize()));
            like.setTag(item.getItem());
            int visible = (item.isShared() || item.isSelected()) ? View.VISIBLE : View.GONE;
            like.setVisibility(visible);
        }
    }

    static class ApkViewHolder extends ViewHolder {

        @BindView(R.id.text_display_name)
        TextView displayName;

        ApkViewHolder(@NonNull View view, @NonNull FlexibleAdapter adapter) {
            super(view, (MediaAdapter) adapter);
        }

        @Override
        void bind(int position, MediaItem item) {
            super.bind(position, item);
            Apk apk = (Apk) item.getItem();
            Drawable icon = AndroidUtil.getApplicationIcon(getContext(), apk.getPackageName());
            this.icon.setImageDrawable(icon);
            displayName.setText(apk.getDisplayName());
        }
    }

    static class ImageViewHolder extends ViewHolder {

        int size;

        ImageViewHolder(@NonNull View view, @NonNull FlexibleAdapter adapter) {
            super(view, (MediaAdapter) adapter);
            size = DisplayUtil.getScreenWidthInPx(getContext()) / 4;
        }

        @Override
        void bind(int position, MediaItem item) {
            super.bind(position, item);
            Image image = (Image) item.getItem();
            Timber.v("Uri %s", image.getUri());
            Timber.v("Thumb Uri %s", image.getThumbUri());
            FrescoUtil.loadImage(icon, image.getUri(), size, true);
        }
    }
}
