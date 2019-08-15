package com.dreampany.tools.data.source.repository

import com.dreampany.frame.misc.Memory
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.tools.data.model.App
import com.dreampany.tools.data.source.api.MediaDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Memory memory: MediaDataSource<App>
) : MediaRepository<App>(rx, rm, memory) {
}