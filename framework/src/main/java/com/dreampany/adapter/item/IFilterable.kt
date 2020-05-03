package com.dreampany.adapter.item

/**
 * Created by roman on 6/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface IFilterable {
    fun filter(filterText: CharSequence?) : Boolean
}