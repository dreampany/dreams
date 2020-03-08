package com.dreampany.tools.ui.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RadioGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extensions.gone
import com.dreampany.framework.misc.extensions.setOnSafeClickListener
import com.dreampany.framework.misc.extensions.toHtml
import com.dreampany.framework.misc.extensions.visible
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.AppType
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.AppAdapter
import com.dreampany.tools.ui.adapter.question.QuestionAdapter
import com.dreampany.tools.ui.model.question.QuestionItem
import com.github.nikartm.button.FitButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import jp.shts.android.library.TriangleLabelView
import java.io.Serializable
import java.lang.ref.WeakReference


/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppItem
private constructor(
    item: App,
    var lockStatus: Boolean,
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem<AppItem.ViewHolder, App, String>(item, layoutId) {

    companion object {
        fun getItem(item: App, lockStatus: Boolean = Constants.Default.BOOLEAN): AppItem {
            val layoutId: Int
            if (lockStatus) {
                layoutId = R.layout.item_lock_app
            } else {
                layoutId = R.layout.item_app
            }
            return AppItem(item, lockStatus, layoutId)
        }
    }

    private var icon: WeakReference<Drawable>? = null
    var locked: Boolean = false

    fun getIcon(context: Context): Drawable? {
        if (icon?.get() == null) {
            var drawable = AndroidUtil.getApplicationIcon(context, item.id)
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)
            }
            drawable?.run {
                icon = WeakReference(this)
            }
        }
        return icon?.get()
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        if (lockStatus) return LockViewHolder(view, adapter)
        return ItemViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    abstract class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>,
        stickyHeader: Boolean = Constants.Default.BOOLEAN
    ) : BaseItem.ViewHolder(view, adapter, stickyHeader) {

        protected var adapter: AppAdapter
        protected val height: Int

        protected var imageIcon: CircleImageView
        protected var textName: MaterialTextView
        protected var textSize: MaterialTextView
        protected var buttonOpen: FitButton
        protected var labelType: TriangleLabelView

        init {
            this.adapter = adapter as AppAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())

            imageIcon = view.findViewById(R.id.image_icon)
            textName = view.findViewById(R.id.text_name)
            textSize = view.findViewById(R.id.text_size)
            buttonOpen = view.findViewById(R.id.button_open)
            labelType = view.findViewById(R.id.label_type)

            view.layoutParams.height = height

            imageIcon.setOnSafeClickListener {
                open()
            }
            textName.setOnSafeClickListener {
                open()
            }
            buttonOpen.setOnSafeClickListener {
               open()
            }
        }

        protected fun open() {
            this.adapter.getItem(adapterPosition)?.run {
                adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this,
                    action = Action.OPEN
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            val uiItem = item as AppItem
            val item = uiItem.item
            val drawlable = uiItem.getIcon(context)
            if (drawlable != null) {
                imageIcon.setImageDrawable(drawlable)
            } else {
                imageIcon.setImageResource(R.drawable.ic_android_black_24dp)
            }

            textName.text = item.name
            textSize.text = DataUtilKt.formatReadableSize(item.size)
            labelType.primaryText = item.appType.name

            when (item.appType) {
                AppType.SYSTEM -> {
                    labelType.setTriangleBackgroundColorResource(R.color.material_red500)
                }
                AppType.USER -> {
                    labelType.setTriangleBackgroundColorResource(R.color.material_green500)
                }
            }
        }
    }

    internal class ItemViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        AppItem.ViewHolder(view, adapter) {

        private var buttonDetails: FitButton

        init {
            buttonDetails = view.findViewById(R.id.button_details)

            buttonDetails.setOnSafeClickListener {
                this.adapter.getItem(adapterPosition)?.run {
                    super.adapter.uiItemClickListener?.onUiItemClick(
                        view = view,
                        item = this,
                        action = Action.DETAILS
                    )
                }
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as AppItem
            val item = uiItem.item


        }
    }

    internal class LockViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        AppItem.ViewHolder(view, adapter) {

        private var buttonLock: AppCompatImageButton

        init {
            buttonLock = view.findViewById(R.id.button_lock)

            buttonLock.setOnSafeClickListener {
                this.adapter.getItem(adapterPosition)?.run {
                    super.adapter.uiItemClickListener?.onUiItemClick(
                        view = view,
                        item = this,
                        action = Action.LOCK
                    )
                }
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH, T, S>> bind(
            position: Int,
            item: I
        ) {
            super.bind(position, item)
            val uiItem = item as AppItem
            val item = uiItem.item

            if (uiItem.locked) {
                buttonLock.setImageResource(R.drawable.ic_lock_black_24dp)
            } else {
                buttonLock.setImageResource(R.drawable.ic_lock_open_black_24dp)
            }
        }
    }
}