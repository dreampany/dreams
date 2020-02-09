package com.dreampany.tools.ui.model.resume

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ServerItem
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-01-11
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ProfileItem
private constructor(
    item: Resume,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<ProfileItem.ViewHolder, Resume, String>(item, layoutId) {

    companion object {
        fun getItem(item: Resume): ProfileItem {
            return ProfileItem(
                item,
                R.layout.item_server
            )
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ServerItem
        return Objects.equal(this.item.id, item.item.id)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(
            view,
            adapter
        )
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        init {

        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH,T,  S>>
                bind(position: Int, item: I) {
            val uiItem = item as ProfileItem
            val item = uiItem.item


        }
    }
}