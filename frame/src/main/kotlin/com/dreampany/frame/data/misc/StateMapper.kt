package com.dreampany.frame.data.misc

import com.dreampany.frame.data.model.State
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.misc.StoreAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StateMapper @Inject constructor(
    @StoreAnnote val map: SmartMap<String, State>,
    @StoreAnnote val cache: SmartCache<String, State>
) {

    fun isExists(id: String): Boolean {
        return map.contains(id)
    }

    fun isExists(id: String, type: String, subtype: String, state: String): Boolean {
        if (isExists(id)) {
            val item = getItem(id)
            return item.hasProperty(type, subtype, state)
        }
        return false
    }

    fun putItem(item: State) {
        map.put(item.id, item)
    }

    fun getItem(id: String): State {
        return map.get(id)
    }

    fun getItem(id: String, type: String, subtype: String, state: String): State? {
        return if (isExists(id, type, subtype, state)) {
            getItem(id)
        } else null
    }
}