package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.framework.data.source.api.DataSourceRx
import com.dreampany.tools.data.model.Media

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface MediaDataSource<T : Media> : DataSource<T>, DataSourceRx<T> {
}