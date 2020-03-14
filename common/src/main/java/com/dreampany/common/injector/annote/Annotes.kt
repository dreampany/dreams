package com.dreampany.common.injector.annote

import javax.inject.Qualifier

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PrefAnnote

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomAnnote

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteAnnote
