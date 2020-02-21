package com.dreampany.tools.ui.model.question

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.StationItem
import com.dreampany.tools.ui.model.resume.ResumeItem
import com.google.common.base.Objects
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class QuestionItem
private constructor(
    item: Question,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<QuestionItem.ViewHolder, Question, String>(item, layoutId) {

    companion object {
        fun getItem(item: Question): QuestionItem {
            return QuestionItem(item, R.layout.item_station)
        }
    }

    override fun hashCode(): Int {
        return Objects.hashCode(item.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as QuestionItem
        return Objects.equal(this.item.id, item.item.id)
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {
        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {

        }

    }

    override fun createViewHolder(
        view: View?,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filter(constraint: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}