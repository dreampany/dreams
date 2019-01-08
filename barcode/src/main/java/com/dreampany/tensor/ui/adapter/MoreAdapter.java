package com.dreampany.tensor.ui.adapter;


import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.tensor.data.enums.MoreType;
import com.dreampany.tensor.ui.model.MoreItem;

/**
 * Created by air on 24/2/18.
 */

public class MoreAdapter extends SmartAdapter<MoreItem> {

    public MoreAdapter(Object listener) {
        super(listener);
    }

    public void load() {
        MoreItem apps = new MoreItem(MoreType.APPS);
        MoreItem rate = new MoreItem(MoreType.RATE_US);
        MoreItem about = new MoreItem(MoreType.ABOUT_US);
        MoreItem feedback = new MoreItem(MoreType.FEEDBACK);
        MoreItem settings = new MoreItem(MoreType.SETTINGS);

        addItem(apps);
        addItem(rate);
        addItem(about);
        addItem(feedback);
        addItem(settings);
    }

}
