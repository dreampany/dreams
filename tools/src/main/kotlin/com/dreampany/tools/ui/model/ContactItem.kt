package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.api.theme.ColorManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.ui.widget.TextDrawable
import com.dreampany.framework.util.TextUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.ContactAdapter
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
    colors: ColorManager,
    item: Contact,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ContactItem.ViewHolder, Contact, String>(item, layoutId) {

    var block: Boolean = false
    var color: Int = 0

    init {
        color = colors.getNextColor(Type.CONTACT)
    }

    companion object {
        fun getItem(colors: ColorManager, item: Contact): ContactItem {
            return ContactItem(colors, item, R.layout.item_contact)
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

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: ContactAdapter
        private var imageIcon: AppCompatImageView
        private var textTitle: AppCompatTextView

        init {
            this.adapter = adapter as ContactAdapter

            imageIcon = view.findViewById(R.id.image_icon)
            textTitle = view.findViewById(R.id.text_title)

            view.setOnClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.CLICK
                )
                true
            }

            view.setOnLongClickListener {
                this.adapter.uiItemClickListener?.onUiItemLongClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.LONG_CLICK
                )
                true
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ContactItem
            val item = uiItem.item

            if (adapter.isSelected(uiItem)) {
                imageIcon.setImageResource(R.drawable.ic_check_circle_black_24dp)
            } else {
                val drawable = TextDrawable.builder().buildRound(
                    Constants.Sep.PLUS + TextUtilKt.getFirst(item.id),
                    uiItem.color
                )
                imageIcon.setImageDrawable(drawable)
            }

            textTitle.text = Constants.Sep.PLUS + item.id
        }
    }
}