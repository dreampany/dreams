package com.dreampany.tools.ui.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.DataUtilKt
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.AppType
import com.dreampany.tools.data.model.App
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.AppAdapter
import com.github.nikartm.button.FitButton
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
    @LayoutRes layoutId: Int = Constants.Default.INT
) : BaseItem< AppItem.ViewHolder,App, String>(item, layoutId) {

    companion object {
        fun getItem(item: App): AppItem {
            return AppItem(item, R.layout.item_app)
        }
    }

    private var icon: WeakReference<Drawable>? = null

    fun getIcon(context: Context): Drawable? {
        if (icon == null || icon!!.get() == null) {
            var drawable = AndroidUtil.getApplicationIcon(context, item.id)
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)
            }
            icon = WeakReference(drawable!!)
        }
        return icon!!.get()
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String?): Boolean {
        return false
    }

    class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : BaseItem.ViewHolder(view, adapter) {

        private val height: Int

        private var adapter: AppAdapter
        private var imageIcon: CircleImageView
        private var textName: AppCompatTextView
        private var textSize: AppCompatTextView
        private var buttonOpen: FitButton
        private var buttonDetails: FitButton
        private var labelType: TriangleLabelView

        init {
            this.adapter = adapter as AppAdapter
            height = getSpanHeight(this.adapter.getSpanCount(), this.adapter.getItemOffset())

            imageIcon = view.findViewById(R.id.image_icon)
            textName = view.findViewById(R.id.text_market)
            textSize = view.findViewById(R.id.text_size)
            buttonOpen = view.findViewById(R.id.button_open)
            buttonDetails = view.findViewById(R.id.button_details)
            labelType = view.findViewById(R.id.label_type)

            view.layoutParams.height = height


/*            view.setOnClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(this.adapter.getItem(adapterPosition)!!)
            }
            view.setOnLongClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(this.adapter.getItem(adapterPosition)!!)
                true
            }*/

            buttonOpen.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.OPEN
                )
            }

            buttonDetails.setOnSafeClickListener {
                this.adapter.uiItemClickListener?.onUiItemClick(
                    view = view,
                    item = this.adapter.getItem(adapterPosition)!!,
                    action = Action.DETAILS
                )
            }
        }

        override fun <VH : BaseItem.ViewHolder, T : Base, S : Serializable, I : BaseItem<VH,T,  S>> bind(
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
}