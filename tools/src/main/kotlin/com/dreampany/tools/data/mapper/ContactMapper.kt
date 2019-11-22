package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.source.api.ContactDataSource
import com.dreampany.tools.data.source.pref.BlockPref
import com.dreampany.tools.injector.annotation.ContactAnnote
import com.dreampany.tools.injector.annotation.ContactItemAnnote
import com.dreampany.tools.ui.model.ContactItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ContactMapper
@Inject constructor(
    private val context: Context,
    private val pref: BlockPref,
    @ContactAnnote private val map: SmartMap<String, Contact>,
    @ContactAnnote private val cache: SmartCache<String, Contact>,
    @ContactItemAnnote private val uiMap: SmartMap<String, ContactItem>,
    @ContactItemAnnote private val uiCache: SmartCache<String, ContactItem>
) : Mapper() {

    @Synchronized
    fun getUiItem(id: String): ContactItem? {
        return uiMap.get(id)
    }

    @Synchronized
    fun putUiItem(id: String, uiItem: ContactItem) {
        uiMap.put(id, uiItem)
    }

    @Synchronized
    @Throws(Throwable::class)
    fun getItem(input: Store, source: ContactDataSource): Contact? {
        var out: Contact? = map.get(input.id)
        if (out == null) {
            out = source.getItem(input.id)
            map.put(input.id, out)
        }
        if (out == null) return null
        return out
    }

    @Synchronized
    @Throws(Throwable::class)
    fun getItem(countryCode: String?, phoneNumber: String?, source: ContactDataSource): Contact? {
        if (countryCode.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) return null
        val id = countryCode.plus(phoneNumber)
        var out: Contact? = map.get(id)
        if (out == null) {
            out = source.getItem(id)
            if (out != null) {
                map.put(id, out)
            }
        }
        if (out == null) {
            out = Contact(id)
            map.put(id, out)
        }

        out.countryCode = countryCode
        out.phoneNumber = phoneNumber

        return out
    }
}