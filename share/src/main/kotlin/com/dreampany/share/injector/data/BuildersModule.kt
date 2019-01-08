package com.dreampany.share.injector.data

import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.Room
import com.dreampany.media.data.model.Apk
import com.dreampany.media.data.model.Image
import com.dreampany.media.injector.data.MediaModule
import com.dreampany.share.data.misc.ApkStateMapper
import com.dreampany.share.data.misc.ImageStateMapper
import com.dreampany.share.data.source.ShareDataSource
import com.dreampany.share.data.source.local.share.ApkRoomShareDataSource
import com.dreampany.share.data.source.local.share.ImageRoomShareDataSource
import com.dreampany.share.injector.vm.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, MediaModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    @Room
    fun provideApkRoomShareDataSource(mapper: ApkStateMapper,
                                  stateRepo: StateRepository): ShareDataSource<Apk> {
        return ApkRoomShareDataSource(mapper, stateRepo)
    }

    @Singleton
    @Provides
    @Room
    fun provideImageRoomShareDataSource(mapper: ImageStateMapper,
                                         stateRepo: StateRepository): ShareDataSource<Image> {
        return ImageRoomShareDataSource(mapper, stateRepo)
    }
}
