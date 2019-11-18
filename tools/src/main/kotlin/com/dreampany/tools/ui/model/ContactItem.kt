package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ContactAdapter
import com.facebook.drawee.view.SimpleDraweeView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2019-11-17
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ContactItem
private constructor(
    item: Contact,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ContactItem.ViewHolder, Contact, String>(item, layoutId) {

    companion object {
        fun getItem(item: Contact): ContactItem {
            return ContactItem(item, R.layout.item_contact)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ContactItem.ViewHolder {
        return ContactItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return item.name?.contains(constraint, true) ?: false
    }

    class ViewHolder( view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: ContactAdapter
        //private var icon: SimpleDraweeView
        //private var name: AppCompatTextView

        init {
            this.adapter = adapter as ContactAdapter

            //icon = view.findViewById(R.id.image_icon)
            //name = view.findViewById(R.id.text_name)
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ContactItem
            val item = uiItem.item


        }
    }
}