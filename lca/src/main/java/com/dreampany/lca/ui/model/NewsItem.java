package com.dreampany.lca.ui.model;

import androidx.annotation.LayoutRes;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.frame.util.FrescoUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.News;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class NewsItem extends BaseItem<News, NewsItem.ViewHolder> {

    private NewsItem(News news, @LayoutRes int layoutId) {
        super(news, layoutId);
    }

    public static NewsItem getItem(News news) {
        return new NewsItem(news, R.layout.item_news);
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        NewsItem item = (NewsItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        if (item.getTitle().toLowerCase().startsWith(((String) constraint).toLowerCase())) {
            return true;
        }
        return false;
    }

    static class ViewHolder extends BaseItem.ViewHolder {

        SimpleDraweeView icon;
        TextView title;
        TextView body;
        TextView source;
        TextView time;

        ViewHolder(@NotNull View view, @NotNull FlexibleAdapter adapter) {
            super(view, adapter);
            icon = view.findViewById(R.id.image_icon);
            title = view.findViewById(R.id.text_title);
            body = view.findViewById(R.id.text_body);
            source = view.findViewById(R.id.text_source);
            time = view.findViewById(R.id.text_time);
        }

        void bind(NewsItem item) {
            News news = item.getItem();
            FrescoUtil.loadImage(icon, news.getImageUrl(), true);
            title.setText(news.getTitle());
            body.setText(news.getBody());
            source.setText(news.getSource());
            String publishTimeString = (String) DateUtils.getRelativeTimeSpanString(
                    news.getPublishedOn() * 1000,
                    TimeUtil.currentTime(),
                    DateUtils.MINUTE_IN_MILLIS);
            time.setText(publishTimeString);
        }
    }
}
