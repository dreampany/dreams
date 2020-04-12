package com.dreampany.common.ui.adapter

import androidx.annotation.Nullable
import com.dreampany.adapter.FlexibleAdapter
import com.dreampany.common.ui.model.BaseItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
open class SmartAdapter<VH : BaseItem.ViewHolder<T>, T : BaseItem<VH, T>>(
    @Nullable listeners: Any?
) : FlexibleAdapter<VH, T>(listeners) {
}