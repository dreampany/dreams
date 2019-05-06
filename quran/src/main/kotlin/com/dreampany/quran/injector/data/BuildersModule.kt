package com.dreampany.quran.injector.data

import com.dreampany.quran.injector.vm.ViewModelModule
import com.dreampany.frame.injector.data.FrameModule
import dagger.Module


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {
}
