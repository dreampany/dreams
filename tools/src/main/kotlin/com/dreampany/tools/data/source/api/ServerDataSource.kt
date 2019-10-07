package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.tools.data.model.Server
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ServerDataSource : DataSource<Server> {

    fun getRandomItem(): Server?

    fun getRandomItemRx(): Maybe<Server>
}