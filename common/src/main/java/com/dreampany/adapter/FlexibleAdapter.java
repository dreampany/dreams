package com.dreampany.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by roman on 28/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class FlexibleAdapter extends RecyclerView.Adapter {

    public OnItemClickListener cickListener;
    public OnItemLongClickListener longClickListener;

    interface OnItemClickListener {
        boolean onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}
