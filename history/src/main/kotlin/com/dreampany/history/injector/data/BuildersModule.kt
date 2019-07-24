package com.dreampany.history.injector.data

import com.dreampany.history.injector.vm.ViewModelModule
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
