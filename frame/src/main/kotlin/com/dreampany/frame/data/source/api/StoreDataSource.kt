package com.dreampany.frame.data.source.api

import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Store
import io.reactivex.Maybe

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StoreDataSource : DataSource<Store> {

    fun isExists(id: String, type: Type, subtype: Subtype, state: State): Boolean

    fun isExistsRx(id: String, type: Type, subtype: Subtype, state: State): Maybe<Boolean>

    fun getCount(id: String, type: Type, subtype: Subtype): Int

    fun getCountRx(id: String, type: Type, subtype: Subtype): Maybe<Int>

    fun getCountByType(type: Type, subtype: Subtype, state: State): Int

    fun getCountByTypeRx(type: Type, subtype: Subtype, state: State): Maybe<Int>

    fun getItem(id: String, type: Type, subtype: Subtype, state: State): Store?

    fun getItemRx(id: String, type: Type, subtype: Subtype, state: State): Maybe<Store>

}