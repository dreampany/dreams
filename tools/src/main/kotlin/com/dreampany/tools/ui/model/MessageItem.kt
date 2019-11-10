package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.data.model.Message
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.MessageAdapter
import java.io.Serializable

/**
 * Created by roman on 2019-11-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class MessageItem < VH : MessageItem.ViewHolder, T : Message, S : Serializable>(
    item: T,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<VH, T, S>(item, layoutId) {

    abstract class ViewHolder(view: View, adapter: MessageAdapter, stickyHeader: Boolean = Constants.Default.BOOLEAN) :
        BaseItem.ViewHolder(view, adapter, stickyHeader) {

        var adapter: MessageAdapter
        //private var time: AppCompatTextView

        init {
            this.adapter = adapter as MessageAdapter
            //time = view.findViewById(R.id.view_time)
        }

/*        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {

        }*/

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {

        }
    }
}