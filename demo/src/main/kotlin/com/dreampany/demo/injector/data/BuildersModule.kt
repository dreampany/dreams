package com.dreampany.demo.injector.data

import com.dreampany.demo.injector.vm.ViewModelModule
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.translation.injector.TranslationModule
import dagger.Module


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, TranslationModule::class,DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {
}
