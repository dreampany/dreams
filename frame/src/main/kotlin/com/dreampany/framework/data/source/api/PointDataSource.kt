package com.dreampany.framework.data.source.api

import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Point
import io.reactivex.Maybe

/**
 * Created by roman on 2019-09-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface PointDataSource : DataSource<Point> {

    fun getItem(id: String, type: Type, subtype: Subtype, level: Level): Point?

    fun getItemRx(id: String, type: Type, subtype: Subtype, level: Level): Maybe<Point>
}