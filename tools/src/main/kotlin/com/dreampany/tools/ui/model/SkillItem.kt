package com.dreampany.tools.ui.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.model.Skill
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-01-15
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class SkillItem private constructor(
    item: Skill,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<SkillItem.ViewHolder, Skill, String>(item, layoutId) {

    companion object {
        fun getItem(item: Skill): SkillItem {
            return SkillItem(item, 0)
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
    ): SkillItem.ViewHolder {
        return SkillItem.ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        init {

        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>>
                bind(position: Int, item: I) {
            val uiItem = item as ResumeItem
            val item = uiItem.item


        }
    }
}