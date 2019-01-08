package com.dreampany.scan.injector.data

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.scan.data.model.Scan
import com.dreampany.scan.misc.ScanAnnote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 7/4/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
class SupportModule {

    @Singleton
    @Provides
    @ScanAnnote
    fun provideScanSmartCache(): SmartCache<String, Scan> {
        return SmartCache<String, Scan>();
    }

    @Singleton
    @Provides
    @ScanAnnote
    fun provideScanSmartMap(): SmartMap<String, Scan> {
        return SmartMap<String, Scan>();
    }
}