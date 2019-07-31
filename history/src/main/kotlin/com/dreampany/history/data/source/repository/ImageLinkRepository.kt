package com.dreampany.history.data.source.repository

import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.Room
import com.dreampany.frame.misc.RxMapper
import com.dreampany.history.data.source.api.HistoryDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-30
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageLinkRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room val room: HistoryDataSource,
    @Remote val remote: HistoryDataSource
){
}