package com.dreampany.tube.inject.data

import com.dreampany.framework.inject.annote.Room
import com.dreampany.tube.data.source.api.PageDataSource
import com.dreampany.tube.data.source.room.PageRoomDataSource
import com.dreampany.tube.data.source.room.dao.PageDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 25/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class PageModule {
    @Singleton
    @Provides
    @Room
    fun providePageRoomDataSource(
        dao: PageDao
    ): PageDataSource = PageRoomDataSource(dao)
}