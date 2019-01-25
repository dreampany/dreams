package com.dreampany.share.ui.model;

import android.graphics.drawable.Drawable;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.DisplayUtil;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.NumberUtil;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.R;
import com.dreampany.share.ui.adapter.MediaAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;
import com.like.LikeButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MediaItem extends BaseItem<Media, MediaItem.ViewHolder> {

    private boolean flagged;
    private boolean selected;
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

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isShared() {
        return shared;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        MediaItem item = (MediaItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public boolean filter(Serializable constraint) {
        return item.getDisplayName().toLowerCase().startsWith(((String) constraint).toLowerCase());
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
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        MediaAdapter adapter;
        @BindView(R.id.image_icon)
        SimpleDraweeView icon;
        @BindView(R.id.text_size)
        TextView size;
        @BindView(R.id.button_like)
        LikeButton like;

        ViewHolder(@NotNull View view, @NotNull MediaAdapter adapter) {
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

        ApkViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
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

        ImageViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
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
