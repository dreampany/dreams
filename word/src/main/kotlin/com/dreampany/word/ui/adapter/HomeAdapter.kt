package com.dreampany.word.ui.adapter

import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.model.BaseItem
import com.dreampany.word.data.enums.ItemState
import com.dreampany.word.data.enums.ItemSubtype
import com.dreampany.word.data.enums.ItemType
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

/**
 * Created by Hawladar Roman on 1/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class HomeAdapter(listener: Any) : SmartAdapter<BaseItem<Any, *>>(listener) {

    private lateinit var types: BiMap<BaseItem<Any, *>, Triple<ItemType, ItemSubtype, ItemState>>

    init {
        types = HashBiMap.create()
    }

    fun addItem(item : BaseItem<Any, *>, type : ItemType, subtype: ItemSubtype, state : ItemState) {
        val triple = Triple(type, subtype, state)
        // remove old or updateVisibleItemIf same
        if (types.inverse().containsKey(triple)) {
            if (contains(item)) {
                updateItem(item);
            } else {
                val item = types.inverse().get(triple);
                item?.let { removeItem(it) };
            }
        }

        // if not found then insert freshly
        if (!contains(item)) {
            types.put(item, triple);
            if (state == ItemState.TODAY) {
                addItem(0, item);
            } else {
                addItem(getItemCount(), item);
            }
        }

        fun removeItem(item : BaseItem<Any, *>) {
            types.remove(item);
            removeItem(getGlobalPositionOf(item));
        }
    }
}