package com.dreampany.crime.injector.data

import com.dreampany.crime.data.model.Crime
import com.dreampany.crime.misc.CrimeAnnote
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @CrimeAnnote
    fun provideDemoSmartCache(): SmartCache<String, Crime> {
        return SmartCache()
    }

    @Singleton
    @Provides
    @CrimeAnnote
    fun provideDemoSmartMap(): SmartMap<String, Crime> {
        return SmartMap()
    }
}